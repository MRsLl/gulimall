package com.atguigu.guli.service.trade.util;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WeixinPayProperties {

    //关联的公众号appid
    private String appId;
    //商户号
    private String partner;
    //商户key
    private String partnerKey;
    //回调地址
    private String notifyUrl;
}
