package com.atguigu.guli.service.ucenter.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx.open")
@Data
public class UcenterProperties {

    private String appId;

    private String appSecret;

    private String redirectUri;
}
