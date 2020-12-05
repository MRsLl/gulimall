package com.atguigu.guli.service.trade.service;

import java.util.Map;

public interface WeixinPayService {
    Map createNative(String orderNo, String remoteAddr);
}
