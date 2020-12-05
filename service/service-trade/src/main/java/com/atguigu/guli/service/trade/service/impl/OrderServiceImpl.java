package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.utils.OrderNoUtils;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.feign.EduCourseService;
import com.atguigu.guli.service.trade.feign.UcenterMemberService;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.mapper.PayLogMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-01
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private EduCourseService eduCourseService;

    @Resource
    private UcenterMemberService ucenterMemberService;

    @Resource
    private PayLogMapper payLogMapper;

    @Override
    public Boolean isBuyByCourseId(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",1);

        Integer count = baseMapper.selectCount(queryWrapper);

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String saveOrder(String courseId, String memberId) {
        //查询是否有已创建订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",0);

        Order orderExists = baseMapper.selectOne(queryWrapper);

        if (orderExists != null) {
            return orderExists.getOrderNo();
        }
        //查询课程信息
        R courseR = eduCourseService.getCourseDtoById(courseId);
        System.out.println(courseR.getData().get("courseDto").getClass().getName());
        ObjectMapper objectMapper = new ObjectMapper();
        CourseDto courseDto = objectMapper.convertValue(courseR.getData().get("courseDto"), CourseDto.class);

        if (courseDto == null) {
            throw new GuliException(ResultCodeEnum.REMOTE_CALL_ERROR);
        }

        //查询用户信息
        R memberR = ucenterMemberService.getMemberDtoById(memberId);
        System.out.println(memberR.getData().get("memberDto").getClass().getName());
        MemberDto memberDto = objectMapper.convertValue(memberR.getData().get("memberDto"), MemberDto.class);

        if (memberDto == null) {
            throw new GuliException(ResultCodeEnum.REMOTE_CALL_ERROR);
        }
        //创建订单并插入
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseCover(courseDto.getCover());
        order.setCourseId(courseDto.getId());
        order.setCourseTitle(courseDto.getTitle());
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)).longValue());
        order.setTeacherName(courseDto.getTeacherName());
        order.setMemberId(memberDto.getId());
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());
        order.setPayType(1);
        order.setStatus(0);

        baseMapper.insert(order);

        return order.getOrderNo();
    }

    /**
     * 根据订单编号和用户id 获取订单
     * @param orderNo
     * @param memberId
     * @return
     */
    @Override
    public Order getOrderById(String orderNo, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("member_id",memberId);

        Order order = baseMapper.selectOne(queryWrapper);

        return order;
    }

    /**
     * 根据订单号获取订单
     * @param orderNo
     * @return
     */
    @Override
    public Order getOrderByNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);

        Order order = baseMapper.selectOne(queryWrapper);

        return order;
    }

    /**
     * 更新订单状态为已支付
     * 保存订单日志
     * @param notifyMap
     */
    @Override
    public void updateOrderStatus(Map<String, String> notifyMap) throws ParseException {
        //更新订单支付状态
        String orderNo = notifyMap.get("out_trade_no");
        Order order = this.getOrderByNo(orderNo);

        order.setStatus(1);
        baseMapper.updateById(order);

        //生成支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date payTime = dateFormat.parse(notifyMap.get("time_end"));
        payLog.setPayTime(payTime);

        payLog.setTotalFee(order.getTotalFee());
        payLog.setTransactionId(notifyMap.get("transaction_id"));
        payLog.setTradeState(notifyMap.get("result_code"));
        payLog.setPayType(1);

        Gson gson = new Gson();
        payLog.setAttr(gson.toJson(notifyMap));

        payLogMapper.insert(payLog);

        //更改课程销量
        eduCourseService.updateCourseBuyCount(order.getCourseId());
    }

    /**
     * 根据订单编号获取订单支付状态
     * @param orderNo
     * @return
     */
    @Override
    public Boolean getPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);

        Order order = baseMapper.selectOne(queryWrapper);

        if (order.getStatus() == 1) {
            return true;
        }

        return false;
    }
}
