package com.atjixue.gulimall.member.service;

import com.atjixue.gulimall.member.exception.PhoneExistException;
import com.atjixue.gulimall.member.exception.UsernameExistException;
import com.atjixue.gulimall.member.vo.MemberLoginVo;
import com.atjixue.gulimall.member.vo.MemberRegistVo;
import com.atjixue.gulimall.member.vo.SocialUserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 15:22:16
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void regist(MemberRegistVo vo);

    void checkPhoneUnique(String phone) throws PhoneExistException;

    void checkUsernameUnique(String username) throws UsernameExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUserVo socialUserVo) throws Exception;
}

