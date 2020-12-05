package com.atguigu.guli.service.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {

    private String regionId;
    private String keyId;
    private String keySecret;
    private String templateCode;
    private String signName;
}
