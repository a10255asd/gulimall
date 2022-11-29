package com.atjixue.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atjixue.common.utils.HttpUtils;
import com.atjixue.gulimall.member.dao.MemberLevelDao;
import com.atjixue.gulimall.member.entity.MemberLevelEntity;
import com.atjixue.gulimall.member.exception.PhoneExistException;
import com.atjixue.gulimall.member.exception.UsernameExistException;
import com.atjixue.gulimall.member.vo.MemberLoginVo;
import com.atjixue.gulimall.member.vo.MemberRegistVo;
import com.atjixue.gulimall.member.vo.SocialUserVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atjixue.common.utils.PageUtils;
import com.atjixue.common.utils.Query;

import com.atjixue.gulimall.member.dao.MemberDao;
import com.atjixue.gulimall.member.entity.MemberEntity;
import com.atjixue.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberEntity entity = new MemberEntity();
        MemberDao memberDao = this.baseMapper;
        // 设置默认等级
        MemberLevelEntity levelEntity =  memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());
        // 检查用户名和手机号是否唯一.为了让controller 感知异常可以使用异常机制
        checkPhoneUnique(vo.getPhone());
        checkUsernameUnique(vo.getUserName());

        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());
        // 密码进行加密存储 可逆加密： 通过密文 推算出明文 不可逆加密（加密算法）：通过密文 不能推算出明文
        // 盐值加密 随机值
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);

        memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        MemberDao memberDao = this.baseMapper;
        Integer mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if(mobile > 0){
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException {
        MemberDao memberDao = this.baseMapper;
        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if(count > 0){
            throw new UsernameExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword(); //123456
        // 去数据库查询 select * from ums_member where username =? or mobile =?
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = memberDao.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", loginacct).or().eq("mobile", loginacct));
        if(entity == null){
            // 登录失败
            return null;
        }else{
            // 获取到数据库的password
            //$2a$10$tGwc5N6uCw4KdGf0v2iKruIf.X2r5DuIkJSeMJKQRrM/frTEkbRu.
            String passwordDb = entity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 进行密码匹配
            boolean matches = passwordEncoder.matches(password, passwordDb);
            if(matches){
                return entity;
            }else {
                return null;
            }
        }

    }

    @Override
    public MemberEntity login(SocialUserVo socialUserVo) throws Exception {
        // 具有登录和注册合并逻辑
        Integer uid = socialUserVo.getSocialUid();
        //判断当前社交用户是否已经登录过系统；
        MemberDao memberDao = this.baseMapper;
        MemberEntity member = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
        if(member!= null){
            // 用户注册过
            MemberEntity update = new MemberEntity();
            update.setId(member.getId());
            update.setAccess_token(socialUserVo.getAccess_token());
            update.setExpires_in(socialUserVo.getExpires_in());
            memberDao.updateById(update);
            member.setAccess_token(socialUserVo.getAccess_token());
            member.setExpires_in(socialUserVo.getExpires_in());
            return member;
        }else {
            // 没有查到当前社交用户对应的记录，我们就需要注册一个
            MemberEntity regist = new MemberEntity();
            // 查询当前社交用户的社交账号信息（昵称、性别等）
            try {
                Map<String, String> mapUser = new HashMap<>();
                mapUser.put("access_token",socialUserVo.getAccess_token());
                HttpResponse getUid = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<>(),mapUser);
                if(getUid.getStatusLine().getStatusCode() == 200){
                    String s = EntityUtils.toString(getUid.getEntity());
                    JSONObject object = JSON.parseObject(s);
                    String name = object.getString("name");
                    regist.setNickname(name);
                }
            }catch (Exception e){}
            regist.setSocialUid(socialUserVo.getSocialUid());
            regist.setAccess_token(socialUserVo.getAccess_token());
            regist.setExpires_in(socialUserVo.getExpires_in());
            memberDao.insert(regist);
            return regist;
        }
    }
}