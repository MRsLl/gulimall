package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.common.util.utils.Constant;
import com.atguigu.guli.common.util.utils.FormUtils;
import com.atguigu.guli.common.util.utils.RandomUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController

@Api(tags = "用户短信服务管理")
@RequestMapping(value = "/api/sms")
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("send/{mobile}")
    @ApiOperation("发送短信验证码")
    public R sendCode(@PathVariable String mobile){

        //验证手机号有效
        if (StringUtils.isEmpty(mobile) ||!FormUtils.isMobile(mobile)) {
         return R.setResult(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        //生成随机验证码
        String code = RandomUtils.getFourBitRandom();

        //向手机号发送验证码
        smsService.sendCode(mobile,code);

        //将验证码缓存到redis 数据库中
        String key = Constant.CODE_PREFIX + mobile;
        redisTemplate.opsForValue().set(key,code,30, TimeUnit.MINUTES);

        return R.ok().message("验证码发送成功");
    }
}
