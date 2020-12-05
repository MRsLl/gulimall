package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.util.utils.MD5;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.ws.api.model.MEP;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-29
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public void register(RegisterForm registerForm) {
        String mobile = registerForm.getMobile();
        String nickname = registerForm.getNickname();
        String password = registerForm.getPassword();

        //按手机号从数据库中查询，以验证用户是否已经注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);

        Member selectOne = baseMapper.selectOne(queryWrapper);

        if (selectOne != null) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        Member member = new Member();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("https://gulimall-edu-file.oss-cn-shanghai.aliyuncs.com/avatar/default.jpg");

        baseMapper.insert(member);
    }

    @Override
    public String login(LoginForm loginForm) {
        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);

        Member member = baseMapper.selectOne(queryWrapper);

        //验证账户是否已经注册
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //验证用户输入的密码是否正确
        String MD5Password = MD5.encrypt(password);
        if (!member.getPassword().equals(MD5Password)) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //根据用户信息创建并返回 token
        JwtInfo jwtInfo = new JwtInfo(member.getId(),member.getNickname(),member.getAvatar());
        String token = JwtHelper.createToken(jwtInfo);

        return token;
    }

    /**
     * 根据 openid 查询会员信息
     * @param openid
     * @return
     */
    @Override
    public Member getByOpenId(String openid) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);

        Member member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    /**
     * 根据用户信息 map 集合注册会员
     * @param userInfoMap
     * @return
     */
    @Override
    public Member saveMap(Map userInfoMap) {

        String userOpenId = (String)userInfoMap.get("openid");
        String nickname = (String)userInfoMap.get("nickname");
        int sex = (int)(double)userInfoMap.get("sex");
        String avatar = (String)userInfoMap.get("headimgurl");

        Member member = new Member();
        member.setOpenid(userOpenId);
        member.setNickname(nickname);
        member.setSex(sex);
        member.setAvatar(avatar);

        baseMapper.insert(member);

        return member;
    }

    /**
     * 根据用户id 获取 memberDto
     * @param memberId
     * @return
     */
    @Override
    public MemberDto getMemberDtoById(String memberId) {
        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();

        BeanUtils.copyProperties(member,memberDto);

        return memberDto;
    }

    @Override
    public Integer countRegisterNum(String day) {
        return baseMapper.countRegisterNum(day);
    }
}
