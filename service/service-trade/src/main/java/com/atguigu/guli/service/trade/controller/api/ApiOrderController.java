package com.atguigu.guli.service.trade.controller.api;


import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-01
 */
@RestController
@Api(tags = "用户订单管理")
@RequestMapping("/api/trade/order")
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("保存订单")
    @PostMapping("auth/save/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){

        String memberId = JwtHelper.getId(request);
        String orderNo = orderService.saveOrder(courseId,memberId);

        return R.ok().data("orderNo",orderNo);
    }

    @ApiOperation("根据订单编号 orderNo 获取订单信息")
    @GetMapping("auth/getOrderByNo/{orderNo}")
    public R getOrderByNo(@PathVariable String orderNo,HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Order order = orderService.getOrderById(orderNo,memberId);

        return R.ok().data("item",order);
    }

    @ApiOperation("根据课程id 和用户id 判断用户是否已购买课程")
    @GetMapping("auth/isBuy/{courseId}")
    public R isBuyByCourseId(@PathVariable String courseId,HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Boolean isBuy = orderService.isBuyByCourseId(courseId,memberId);

        return R.ok().data("isBuy",isBuy);
    }

    @ApiOperation("根据订单编号获取订单支付状态")
    @GetMapping("getPayStatus/{orderNo}")
    public R getPayStatus(@PathVariable String orderNo){
        Boolean isPay = orderService.getPayStatus(orderNo);
        return R.ok().data("isPay",isPay);
    }

}

