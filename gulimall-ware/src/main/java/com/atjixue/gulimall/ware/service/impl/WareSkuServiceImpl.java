package com.atjixue.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atjixue.common.exception.NoStockException;
import com.atjixue.common.to.StockDetailTo;
import com.atjixue.common.to.mq.OrderTo;
import com.atjixue.common.to.mq.StockLockedTo;
import com.atjixue.common.utils.R;
import com.atjixue.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.atjixue.gulimall.ware.entity.WareOrderTaskEntity;
import com.atjixue.gulimall.ware.feign.OrderFeignService;
import com.atjixue.gulimall.ware.feign.ProductFeignService;
import com.atjixue.gulimall.ware.service.WareOrderTaskDetailService;
import com.atjixue.gulimall.ware.service.WareOrderTaskService;
import com.atjixue.gulimall.ware.vo.*;
import com.qiniu.util.StringUtils;
import com.rabbitmq.client.Channel;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.common.utils.Query;

import com.atjixue.gulimall.ware.dao.WareSkuDao;
import com.atjixue.gulimall.ware.entity.WareSkuEntity;
import com.atjixue.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")

public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    WareSkuDao wareSkuDao;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    WareOrderTaskService orderTaskService;
    @Autowired
    WareOrderTaskDetailService orderTaskDetailService;
    @Autowired
    OrderFeignService orderFeignService;


    /**
     * 库存自动解锁
     */


    private void unlockStock(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        /**
         * update wms_ware_sku set stock_locked=stocked-1 where sku_id =1 and ware_id = 2
         * */
        wareSkuDao.unlockStock(skuId, wareId, num);
        // 更新库存工作单的状态
        WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity();
        entity.setId(taskDetailId);
        entity.setLockStatus(2);
        orderTaskDetailService.updateById(entity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId:1
         * wareId:2
         * */
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isNullOrEmpty(skuId)) {
            wrapper.eq("sku_id", skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isNullOrEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        // 1、判断 如果还没有这个库存记录，就是新增操作
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId)
                .eq("ware_id", wareId));
        if (entities.size() == 0 || entities == null) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //远程查询sku的名字 ,如果失败 整个事务无须回滚
            //1）、自己catch掉异常
            //TODO 还可以用什么方法让异常出现以后不会滚呢？
            try {
                R r = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) r.get("skuInfo");
                if (r.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }

            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuids) {

        List<SkuHasStockVo> collect = skuids.stream().map((skuId) -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            //查询当前sku的总库存量
            // select sum(stock -stock_locked) from wma_ware_sku where sku_id = 1
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count == null ? false : count > 0);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 为某个订单锁住库存
     * 默认只要是运行时异常都会回滚(rollbackFor = NoStockException.class)
     * <p>
     * 库存解锁的场景
     * 1）、下订单成功，订单过期没有支付被系统取消。被用户手动取消。都要解锁库存
     * <p>
     * 2）、下订单成功，库存锁定成功，但是接下来的业务调用失败。导致订单回滚，之前锁定的库存就要自动解锁
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        /**
         * 保存库存工作单的详情 ，追溯
         * */
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        orderTaskService.save(taskEntity);


        // 1、按照下单的收货地址，找到一个就近仓库，锁定库存
        // 1、 找到每个商品哪个仓库都有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            // 查询这个商品在哪里有库存
            List<Long> wareIds = wareSkuDao.ListWareIdHasSkuStock(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());


        // 2、锁定库存
        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (wareIds == null || wareIds.size() == 0) {
                // 没有任何仓库有库存
                throw new NoStockException(skuId);
            }
            // 1、如果每一个商品都锁定成功，将当前商品锁定了几件的工作单记录，发送给了MQ
            // 2、锁定失败，前面保存的工作单信息，就回滚了。发送出去的消息即使要解锁记录，由于去数据库查不到id，所以就不用解锁。
            // 3、
            for (Long wareId : wareIds) {
                // 成功返回 1 否则是 0
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if (count == 1) {
                    // 锁成功
                    skuStocked = true;
                    // TODO 告诉MQ库存锁定成功
                    WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity(null, skuId, "", hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    orderTaskDetailService.save(entity);
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(taskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(entity, stockDetailTo);
                    // 只发id不行，防止回滚以后找不到数据
                    lockedTo.setDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange"
                            , "stock.locked", lockedTo);
                    break;
                } else {
                    // 当前仓库锁失败，重试下一个仓库
                }
                if (skuStocked == false) {
                    // 当前商品所有仓库 都没有锁住
                    throw new NoStockException(skuId);
                }
            }
        }
        // 肯定全部都是锁定成功的

        return true;
    }

    @Override
    public void unlockStock(StockLockedTo to) {
        System.out.println("收到解锁库存的消息");
        StockDetailTo detail = to.getDetail();
        Long detailId = detail.getId();
        /**
         *  解锁
         *         1、查询数据库关于这个订单的锁定库存信息
         *          有：证明库存锁定成功
         *           解锁： 订单情况。
         *           1、没有这个订单了。必须解锁。
         *           2、有这个订单，不是解锁库存。
         *              订单状态：已取消，解锁库存
         *                      没取消：不能解锁
         *         没有：库存锁定失败，库存回滚。这种情况无需解锁。
         *         、
         *  只要解锁库存的消息失败，一定要告诉服务器解锁失败。
         *
         * */
        WareOrderTaskDetailEntity byId = orderTaskDetailService.getById(detailId);
        if (byId != null) {
            // 解锁
            Long id = to.getId(); //库存工作单的id
            WareOrderTaskEntity taskEntity = orderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn(); // 根据订单号查询
            R r = orderFeignService.getOrderStatus(orderSn);
            if (r.getCode() == 0) {
                // 订单数据返回成功
                OrderVo data = r.getData(new TypeReference<OrderVo>() {
                });
                if (data == null || data.getStatus() != 4) {
                    // 订单不存在 或者  订单已经被取消了，才能解锁库存
                    // 当前库存工作单详情，状态1 已锁定但是未解锁 才可以解锁
                    if (byId.getLockStatus() == 1) {
                        unlockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                    }
                }
            } else {
                // 消息拒绝以后 重新放到队列里面，让别人继续消费解锁
                throw new RuntimeException("远程服务失败");
            }
        } else {
            // 无需解锁
        }
    }

    // 防止订单服务卡顿 导致订单状态一直改不了，库存消息优先到期。查订单状态是新建状态，什么都不做就走了
    // 导致卡顿的订单永远不能解锁库存
    @Transactional
    @Override
    public void unlockStock(OrderTo to) {
        String orderSn = to.getOrderSn();
        // 查一下最新库存解锁状态，防止重复解锁库存
        WareOrderTaskEntity task = orderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = task.getId();
        // 按照库存工作单 找到所有没有解锁的库存 进行解锁
        List<WareOrderTaskDetailEntity> entities = orderTaskDetailService.list(new QueryWrapper<WareOrderTaskDetailEntity>()
                .eq("task_id", id)
                .eq("lock_status", 1));
        for (WareOrderTaskDetailEntity entity : entities) {
            unlockStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum(), entity.getId());
        }
    }

    @Data
    class SkuWareHasStock {
        private Long skuId;
        private List<Long> wareId;
        private Integer num;
    }
}