package com.atguigu.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.config.SmsProperties;
import com.atguigu.guli.service.sms.service.SmsService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    /**
     * 向指定手机号发送验证码
     * @param mobile
     * @param code
     */
    @Override
    public void sendCode(String mobile, String code) {
        //初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getKeyId(), smsProperties.getKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        //设置请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());

        //设置验证码的 key 为"code",存入map
        Map<String, String> map = new HashMap<>();
        map.put("code",code);
        //将code 集合转为json 字符串
        Gson gson = new Gson();
        String jsonCode = gson.toJson(map);
        //把json 形式的验证码设置到请求参数中
        request.putQueryParameter("TemplateParam", jsonCode);

        CommonResponse response = new CommonResponse();
        try {
            //发送请求
            response = client.getCommonResponse(request);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }

        //解析json 字符串格式的返回结果
        HashMap responseMap = gson.fromJson(response.getData(), HashMap.class);
        String responseCode = (String)responseMap.get("Code");
        String responseMessage = (String)responseMap.get("Message");

        if("isv.BUSINESS_LIMIT_CONTROL".equals(responseCode)){
            log.error("短信发送过于频繁 " + "【code】" + responseCode + ", 【message】" + responseMessage);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }
        if (!"OK".equals(responseMessage)) {
            log.error("短信发送失败 " + " - code: " + responseCode + ", message: " + responseMessage);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }
    }
}
