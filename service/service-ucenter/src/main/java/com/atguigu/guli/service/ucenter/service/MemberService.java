package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-29
 */
public interface MemberService extends IService<Member> {

    void register(RegisterForm registerForm);

    String login(LoginForm loginForm);

    Member getByOpenId(String openid);

    Member saveMap(Map userInfoMap);

    MemberDto getMemberDtoById(String memberId);

    Integer countRegisterNum(String day);
}
