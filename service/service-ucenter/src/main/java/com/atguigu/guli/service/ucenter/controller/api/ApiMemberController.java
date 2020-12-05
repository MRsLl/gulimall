package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.common.util.utils.Constant;
import com.atguigu.guli.common.util.utils.FormUtils;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-29
 */
@Api(tags = "用户注册/登录控制")

@RestController
@RequestMapping("/api/ucenter/member")
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据日期统计每天注册人数")
    @GetMapping("countRegisterNum/{day}")
    public R countRegisterNum(@PathVariable String day) {
        Integer num = memberService.countRegisterNum(day);
        return R.ok().data("num",num);
    }

    @ApiOperation("根据用户id 获取 memberDto")
    @GetMapping("getMemberDtoById/{memberId}")
    public R getMemberDtoById(@PathVariable String memberId) {
        MemberDto memberDto = memberService.getMemberDtoById(memberId);
        return R.ok().data("memberDto",memberDto);
    }


    @ApiOperation("用户注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterForm registerForm) {
        String code = registerForm.getCode();
        String mobile = registerForm.getMobile();
        String nickname = registerForm.getNickname();
        String password = registerForm.getPassword();

        //验证注册表单参数非空
        if (StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(password) ||
                !FormUtils.isMobile(mobile)) {
            return R.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        //验证用户输入的验证码是否正确
        String redisKey = Constant.CODE_PREFIX + mobile;
        String redisCode = (String) redisTemplate.opsForValue().get(redisKey);

        if (!redisCode.equals(code)) {
            return R.setResult(ResultCodeEnum.CODE_ERROR);
        }

        memberService.register(registerForm);

        return R.ok().message("注册成功");
    }

    @ApiOperation("用户登录")
    @GetMapping("login")
    public R login(LoginForm loginForm) {
        String mobile = loginForm.getMobile();

        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            return R.setResult(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        String token = memberService.login(loginForm);

        return R.ok().data("token", token).message("登录成功");
    }

    @ApiOperation("由 token 获取用户信息")
    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return R.ok().data("userInfo", jwtInfo);
    }
}

