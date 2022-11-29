package com.atjixue.gulimall.order.feign;

import com.atjixue.gulimall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/6 17:34
 * @PackageName:com.atjixue.gulimall.order.feign
 * @ClassName: MemberFeignService
 * @Description: TODO
 * @Version 1.0
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);


}
