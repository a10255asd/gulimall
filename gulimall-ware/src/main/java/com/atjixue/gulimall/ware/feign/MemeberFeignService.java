package com.atjixue.gulimall.ware.feign;

import com.atjixue.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/7 17:27
 * @PackageName:com.atjixue.gulimall.ware.feign
 * @ClassName: MemeberFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-member")
public interface MemeberFeignService {

    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    //@RequiresPermissions("member:memberreceiveaddress:info")
    R addrInfo(@PathVariable("id") Long id);
}
