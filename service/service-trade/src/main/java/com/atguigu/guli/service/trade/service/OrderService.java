package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-01
 */
public interface OrderService extends IService<Order> {

    Boolean isBuyByCourseId(String courseId, String memberId);

    String saveOrder(String courseId, String memberId);

    Order getOrderById(String orderNo, String memberId);

    Order getOrderByNo(String orderNo);

    void updateOrderStatus(Map<String, String> notifyMap) throws ParseException;

    Boolean getPayStatus(String orderNo);
}
