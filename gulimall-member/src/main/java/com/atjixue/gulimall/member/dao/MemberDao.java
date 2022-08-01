package com.atjixue.gulimall.member.dao;

import com.atjixue.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author liujixue
 * @email 1025519998@qq.com
 * @date 2022-08-01 15:22:16
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
