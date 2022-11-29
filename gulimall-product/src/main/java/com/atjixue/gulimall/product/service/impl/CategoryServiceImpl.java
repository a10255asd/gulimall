package com.atjixue.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atjixue.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atjixue.gulimall.product.service.CategoryBrandRelationService;
import com.atjixue.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.common.utils.Query;

import com.atjixue.gulimall.product.dao.CategoryDao;
import com.atjixue.gulimall.product.entity.CategoryEntity;
import com.atjixue.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;



@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树型结构
        //2.1 找到所有的1级分类，所有的1级分类 父分类都是0。使用stream api 过滤收集,只有一个参数，小括号可以省略，一条语句是
        //返回值，大括号和return也可以省略。
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        })).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenuById(List<Long> asList) {
        //TODO 检查当前删除的菜单是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return (Long[]) parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 及联更新所有数据
     */
    //@CacheEvict(value = "category",key = "'getLevel1Categorys'")
//    @Caching(evict = {
//            @CacheEvict(value = "category",key = "'getLevel1Categorys'"),
//            @CacheEvict(value = "category",key = "'getCatalogJson'")
//    })
    @CacheEvict(value = "category",allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }
    @Cacheable(value = {"category"},key = "#root.method.name",sync = true)  //代表当前方法的结果需要缓存，如果缓存中有，方法中就不用调用。如果缓存中没有，，调用方法，最后将方法的结果放入缓存
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("getLevel1Categorys......");
        Long l = System.currentTimeMillis();
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    //TODO 产生堆外内存溢出 OutOfDiretMemoryError
    //1)、springboot 2.0 以后默认是使用lettuce作为操作redis的客户端。使用netty进行网络通信
    //2）、lettuce的bug导致堆外内存溢出 -Xm300m netty如果没有指定堆外内存，默认使用-Xm300m
    //3）、永远都会出现，可以通过 -Dio.netty.maxDirectMemory进行设置
    // 解决方案：不能使用 -Dio.netty.maxDirectMemory只去调大堆外内存。
    // 1）、升级lettuce客户端。2）、切换使用jedis
    // redisTemplate
    // lettuce和jedis是操作redis的此层客户端，spring再次封装成redisTemplate
    @Cacheable(value = {"category"},key = "#root.methodName")
    @Override
    public Map<String,List<Catelog2Vo>> getCatalogJson(){
        System.out.println("查询了数据库");
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        // 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 每一个的一级分类，查到一级分类的所有二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map((l2) -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 1、 找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2Vo.catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            // 、封装成指定格式
                            Catelog2Vo.catelog3Vo catelog3Vo = new Catelog2Vo.catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }


    //@Override
    public Map<String, List<Catelog2Vo>> getCatalogJson2() {
        // 给缓存中放JSON字符串，拿出的JSON字符串，还用逆转为能用的对象类型，这就是序列化与饭序列化
        // 1、加入缓存逻辑，缓存中存的所有对象都是JSON字符串
        // JSON的好处是，跨语言跨平台兼容的
        /**
         * 1、增加空结果缓存：解决缓存穿透问题
         * 2、设置过期时间（加随机值）：解决缓存雪崩问题
         * 3、加锁：解决缓存击穿问题
         * */
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            // 2、缓存中没有数据，查询数据库
            //System.out.println("缓存不命中，将要查询数据库");
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbRedisLock();
            return catalogJsonFromDb;
        }
        //System.out.println("缓存命中，直接返回......");
        // 转为指定的对象
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }
    /**
     * 缓存里面的数据如何 和 数据库 保持一致
     * 缓存数据一致性问题
     * 1）、双写模式
     * 2）、失效模式
     * */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbRedissonLock() {
        // 锁的名字，名字一样 锁就一样，锁的粒度越细，锁的速度越快。
        // 锁的粒度： 具体缓存的是某个数据， 11-号商品； product-11-lock  product-12-lock
        RLock lock = redisson.getLock("catalogJson-lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;

    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbRedisLock() {
        //占分布式锁，去redis占坑，
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);

        if (lock) {
            System.out.println("获取分布式锁成功");
            // 加锁成功 ,执行业务
            // 2、设置过期时间,必须和加锁是同步的，是原子操作。
            //redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                dataFromDb = getDataFromDb();
            } finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }

            // 获取值，对比成功删除，这两步必须是原子操作，所以删除锁，应该使用lua脚本解锁
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if(uuid.equalsIgnoreCase(lockValue)){
//                redisTemplate.delete("lock"); //删除锁
//            }
            // lua 脚本解锁

            // 删除锁
            // redis分布式锁，核心两处：第一加锁保证原子性，第二删除锁保证原子性
            //加锁使用set NX EX 命令，使用lua脚本解锁

            return dataFromDb;
        } else {
            //加锁失败 synchronized ()
            //休眠100ms重试
            System.out.println("获取分布式锁失败，等待重试");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }
            return getCatalogJsonFromDbRedisLock(); //自璇的方式
        }
    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)) {
            // 如果缓存不为null 直接返回。
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        System.out.println("查询了数据库");

        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        // 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 每一个的一级分类，查到一级分类的所有二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map((l2) -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 1、 找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2Vo.catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            // 、封装成指定格式
                            Catelog2Vo.catelog3Vo catelog3Vo = new Catelog2Vo.catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        // 3、查到的数据放入缓存,将对象转为JSON，放入缓存中
        String s = JSON.toJSONString(parent_cid);
        redisTemplate.opsForValue().set("catalogJson", s, 1, TimeUnit.DAYS);
        return parent_cid;
    }

    //从数据库查询并封装整个分类数据
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {

        /**
         *
         *   只要是同一把锁，就能锁住需要这个锁的所有线程
         *   1、使用this的方式，synchronized (this)，springboot所有的组件在容器中都是单例。相当于100w个请求同时
         *      访问CategoryServiceImpl，但是CategoryServiceImpl只有一个，所以是可以锁住的，但是这是在单体应用
         *      里面可行的方案，因为分布式部署每一个服务器都会又一个容器实例CategoryServiceImpl，到最后还是会有很多
         *      线程访问数据库，本地锁，只能锁住当前进程，这个时候我们就需要分布式锁。
         *      TODO 本地锁：synchronized，JUC(lock) 只锁我们当前进程，在分布式情况下，想要锁住所有，必须使用分布式锁
         *   2、
         * */

        synchronized (this) {
            // 得到锁以后 应该再去缓存中确定一次，如果没有才需要继续查询
            String catalogJson = redisTemplate.opsForValue().get("catalogJson");
            if (!StringUtils.isEmpty(catalogJson)) {
                // 如果缓存不为null 直接返回。
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
                return result;
            }
            System.out.println("查询了数据库");
            /**
             *1、将数据库的多次查询变为1次
             * */
            List<CategoryEntity> selectList = baseMapper.selectList(null);

            //查出所有的1级分类，按照要求返回
            List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
            // 封装数据
            Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                // 每一个的一级分类，查到一级分类的所有二级分类
                List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
                //封装上面的结果
                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map((l2) -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                        // 1、 找当前二级分类的三级分类封装成vo
                        List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                        if (level3Catelog != null) {
                            List<Catelog2Vo.catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                                // 、封装成指定格式
                                Catelog2Vo.catelog3Vo catelog3Vo = new Catelog2Vo.catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(collect);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }
                return catelog2Vos;
            }));
            // 3、查到的数据放入缓存,将对象转为JSON，放入缓存中
            String s = JSON.toJSONString(parent_cid);
            redisTemplate.opsForValue().set("catalogJson", s, 1, TimeUnit.DAYS);
            return parent_cid;
        }


    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        return collect;
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、找父亲的id 并收集，收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        // 递归查找所有菜单的子菜单

        List<CategoryEntity> children = all.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            // 找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }
}