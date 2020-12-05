package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.common.util.utils.StreamUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.WeixinPayService;
import com.atguigu.guli.service.trade.util.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/weixinPay")
@Api(description = "网站微信支付")
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @GetMapping("createNative/{orderNo}")
    @ApiOperation("生成二维码返回给前端")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {
        //获取客户端ip 地址
        String remoteAddr = request.getRemoteAddr();
        Map map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //解析并获取微信返回的信息
        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");
        HashMap<String, String> returnMap = new HashMap<>();

        //验签，防止伪造回调
        if (WXPayUtil.isSignatureValid(notifyXml, weixinPayProperties.getPartnerKey())) {

            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);
            //检验交易是否成功
            if ("SUCCESS".equals(notifyMap.get("return_code")) && "SUCCESS".equals(notifyMap.get("result_code"))) {

                String orderNo = notifyMap.get("out_trade_no");
                Order order = orderService.getOrderByNo(orderNo);
                //校验订单金额是否一致
                if (order != null && order.getTotalFee().intValue() == Integer.parseInt(notifyMap.get("total_fee"))) {
                    //判断订单状态：保证接口调用的幂等性
                    if (order.getStatus() == 1) {
                        returnMap.put("return_code", "SUCCESS");
                        returnMap.put("return_msg", "OK");
                        String returnXml = WXPayUtil.mapToXml(returnMap);

                        response.setContentType("text/xml");
                        log.warn("通知已处理");
                        return returnXml;
                    } else {
                        System.out.println("更新订单状态被调用");
                        //更新订单状态，保存订单日志
                        orderService.updateOrderStatus(notifyMap);

                        //给微信返回成功的响应
                        returnMap.put("return_code", "SUCCESS");
                        returnMap.put("return_msg", "OK");
                        String returnXml = WXPayUtil.mapToXml(returnMap);

                        response.setContentType("text/xml");
                        log.info("支付成功，通知已处理");
                        return returnXml;
                    }
                }
            }
        }
        //校验失败给微信返回失败响应
        returnMap.put("return_code", "FAIL");
        returnMap.put("return_msg", "校验失败");
        String returnXml = WXPayUtil.mapToXml(returnMap);

        return returnXml;
    }


}