package com.atjixue.gulimall.seckill.scheduled;

import com.atjixue.gulimall.seckill.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 秒杀商品的定时上架
 * 每天晚上3点 上架最近三天需要秒杀的商品
 * 当天00：00：00- 23：59：59
 * 明天00：00：00- 23：59：59
 * 后天 00：00：00- 23：59：59
 */
@Service
@Slf4j
public class SeckillSkuScheduled {
    @Autowired
    RedissonClient redissonClient;

    private final String upload_lock = "seckill:upload:lock";
    @Autowired
    SecKillService secKillService;
    // TODO 幂等性处理
    @Scheduled(cron = "0 * * * * ?")
    public void uploadSeckillSkuLatest3Days(){
        // 重复上架 无需处理
        log.info("上架商品信息");
        // 分布式锁，获取到锁的人才能执行定时任务
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            secKillService.uploadSeckillSkuLatest3Days();
        }finally {
            lock.unlock();
        }
    }
}
