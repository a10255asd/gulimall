package com.atjixue.gulimall.seckill.scheduled;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * EnableScheduling 开启定时任务
 * @Scheduled(cron = "") 开启一个定时任务
 * 定时任务不应该阻塞，默认是阻塞的
 */
@EnableScheduling
@Component
@Slf4j
@EnableAsync
public class HelloSchedule {

    /**
     * 定时任务
     * 定时任务不应该阻塞，默认是阻塞的
     * 1）、可以让业务运行以异步的方式自己提交到线程池
     * 2）、支持定时任务线程池，设置 spring.task.scheduling.pool.size=5
     * 3)、让定时任务异步执行 异步任务
     *
     * 异步任务  @EnableAsync
     * 1@EnableAsync
     * 2、给希望执行的异步方法标注解 @Async
     * 解决 使用异步➕定时任务 完成定时任务不阻塞的功能
     * */
    @Scheduled(cron = "* * * ? * 7")
    @Async
    public void hello() throws InterruptedException {
        log.info("hello");
        Thread.sleep(3000);
    }
}
