package com.atjixue.gulimall.product.web;

import com.atjixue.gulimall.product.entity.CategoryEntity;
import com.atjixue.gulimall.product.service.CategoryService;
import com.atjixue.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @Author LiuJixue
 * @Date 2022/8/19 09:50
 * @PackageName:com.atjixue.gulimall.product.web
 * @ClassName: IndexController
 * @Description: TODO
 * @Version 1.0
 */
@Controller
@Slf4j
public class IndexController {

    @Autowired
    RedissonClient redisson;

    @Autowired
    CategoryService categoryService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        // TODO 1、查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        // 视图解析器进行拼串：classpath:/templates/ + 返回值  +  .html
        model.addAttribute("categorys", categoryEntities);
        return "index";
    }

    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

    @ResponseBody
    @GetMapping("hello")
    public String hello() {
        //1、 获取一把锁，只要锁的名字一样就是用一把锁
        RLock lock = redisson.getLock("my-lock");
        //2、加锁
        //lock.lock(); // 阻塞式等待，默认加的锁都是30s
        // 1）、 锁的自动续期，如果业务超长，运行期间自动给锁续上新的30s，不用担心业务时间长锁自动过期被删掉
        // 2）、加锁的业务只要运行完成 就不会给当前锁续期，即使不手动解锁，锁也会默认在30s以后自动删除
        lock.lock(10, TimeUnit.SECONDS); //十秒钟自动解锁，自动解锁时间一定要大于业务的执行时间。
        //问题：lock.lock(10, TimeUnit.SECONDS);  锁时间到了以后，不会自动续期
        // 1、如果我们传递了锁的超时时间就，发送给redis执行脚本，默认时间就是我们指定的时间
        //2、如果我们未指定锁的超时时间，就使用30* 1000 LockWatchdog 看门狗的默认时间
        // 只要占锁成功，就会启动一个定时任务，重新给锁设置过期时间，新的过期时间就是看门狗的默认时间
        //internalLockLeaseTime 看门狗时间/3 10s续一次期，每隔10s就会自动再次续期，续成30s

        // 最佳实战：
        // 推荐使用 lock.lock(10, TimeUnit.SECONDS);,省掉了整个续期操作。手动解锁。
        try {
            System.out.println("加锁成功，执行业务" + Thread.currentThread().getId());
            Thread.sleep(3000);
        } catch (Exception e) {

        } finally {
            //解锁  假设解锁代码没有运行redis 会不会死锁
            System.out.println("释放锁..." + Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";
    }

    // 保证一定能读到最新数据，修改期间 写锁是一个排他锁(互斥锁)。读锁是一个共享锁。
    //写锁没释放，读就必须等待
    // 写 + 读 等待写锁释放
    //写 + 写 阻塞方式
    // 读 + 写 ：有读锁 写也需要等待
    // 读 + 读 ：无锁 并发读只会在redis中记录好当前的读锁
    // 只要有写的存在，都必须等待
    @ResponseBody
    @GetMapping("/write")
    public String writeValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String s = "";
        RLock rLock = lock.writeLock();
        try {
            // 1、 改数据 加写锁，读数据加读锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
        }
        return s;
    }

    @ResponseBody
    @GetMapping("/read")
    public String readValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        // 加读锁
        RLock rLock = lock.readLock();
        String s = "";
        rLock.lock();
        try {
            s = redisTemplate.opsForValue().get("writeValue");
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    /**
     * 放假，锁门
     * 全都走完了才能锁门
     * 5个班 全部走完可以锁大门
     */
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5);
        door.await(); //等待闭锁都完成
        return "放假了......";
    }


    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id) {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown(); //计数减一
        return id + "班的人都走了";
    }

    /**
     * 车库停车
     * 3车位
     *来一辆占用一个，走一辆释放一个，想要停车得看车位够不够
     * 信号量可以用来做限流
     * */
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        //park.acquire();//获取一个信号，获取一个值,占一个车位
        boolean b = park.tryAcquire();
        if(b){
            // 执行业务
        }else{
            return "error";
        }
        return "ok=>" + b;
    }

    @GetMapping("/go")
    @ResponseBody
    public String go() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.release(); //释放一个信号， 释放一个车位
        return "ok";
    }


}
