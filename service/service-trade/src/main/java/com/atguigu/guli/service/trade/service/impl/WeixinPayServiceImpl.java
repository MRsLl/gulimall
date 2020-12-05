package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.utils.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.WeixinPayService;
import com.atguigu.guli.service.trade.util.WeixinPayProperties;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private WeixinPayProperties weixinPayProperties;
    @Resource
    private OrderMapper orderMapper;

    @Override
    public Map createNative(String orderNo, String remoteAddr) {
        //根据订单编号 orderNo 查询订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        Order order = orderMapper.selectOne(queryWrapper);

        HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");
        //封装参数map
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weixinPayProperties.getAppId());
        paramMap.put("mch_id", weixinPayProperties.getPartner());
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body", "谷粒在线-购买课程：" + order.getCourseTitle());
        paramMap.put("out_trade_no", orderNo);
        paramMap.put("total_fee", order.getTotalFee() + "");
        paramMap.put("spbill_create_ip", remoteAddr);
        //回调地址，通知地址
        paramMap.put("notify_url", weixinPayProperties.getNotifyUrl());
        paramMap.put("trade_type", "NATIVE ");

        try {
        //将参数map 转为签名后的xml 字符串
        String signedXml = WXPayUtil.generateSignedXml(paramMap, weixinPayProperties.getPartnerKey());
        log.info("\n signedXml：\n" + signedXml);

        //将参数的xml字符串设置到请求体中
        client.setXmlParam(signedXml);
        client.setHttps(true);

            //向微信发送post 请求，获取xml 格式的响应
            client.post();
            String responseXml = client.getContent();

            //将xml 格式的响应转化为map 集合
            Map<String, String> responseMap = WXPayUtil.xmlToMap(responseXml);
            //判断微信是否成功响应
            if ("FAIL".equals(responseMap.get("return_code")) || "FAIL".equals(responseMap.get("result_code"))) {
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            //封装要返回的数据集合
            HashMap<String, Object> returnMap = new HashMap<>();
            returnMap.put("resultCode",responseMap.get("result_code"));
            returnMap.put("codeUrl", responseMap.get("code_url"));
            returnMap.put("orderNo", orderNo);
            returnMap.put("totalFee", order.getTotalFee());
            returnMap.put("courseId", order.getCourseId());

            return returnMap;
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }
}
