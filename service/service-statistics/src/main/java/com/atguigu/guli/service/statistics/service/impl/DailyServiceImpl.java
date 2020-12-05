package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.feign.UcenterMemberService;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javafx.scene.input.DataFormat;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-04
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Resource
    private UcenterMemberService ucenterMemberService;

    @Override
    public void createStatisticsByDay(String day){
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);

        baseMapper.delete(queryWrapper);

        Integer num = (Integer) ucenterMemberService.countRegisterNum(day).getData().get("num");

        Daily daily = new Daily();

        daily.setDateCalculated(day);
        daily.setRegisterNum(num);
        daily.setCourseNum(RandomUtils.nextInt(100,200));

        daily.setLoginNum(RandomUtils.nextInt(100,200));
        daily.setVideoViewNum(RandomUtils.nextInt(100,200));

        baseMapper.insert(daily);
    }

    /**
     * 封装前端显示的统计数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public Map<String, Map> showChart(String begin, String end) {
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        queryWrapper.orderByAsc("date_calculated");

        List<Daily> dailyList = baseMapper.selectList(queryWrapper);

        Map<String, Map> map = new HashMap<>();

        Map<String, List> registerMap = new HashMap<>();
        Map<String, List> loginMap = new HashMap<>();
        Map<String, List> videoViewMap = new HashMap<>();
        Map<String, List> courseMap = new HashMap<>();

        ArrayList dateList = new ArrayList();
        ArrayList registerList = new ArrayList();
        ArrayList loginList = new ArrayList();
        ArrayList videoViewList = new ArrayList();
        ArrayList courseList = new ArrayList();

        for (Daily daily : dailyList) {
            registerList.add(daily.getRegisterNum());
            loginList.add(daily.getLoginNum());
            videoViewList.add(daily.getVideoViewNum());
            courseList.add(daily.getVideoViewNum());
            dateList.add(daily.getDateCalculated());
        }

        registerMap.put("xData",dateList);
        registerMap.put("yData",registerList);
        loginMap.put("xData",dateList);
        loginMap.put("yData",loginList);
        videoViewMap.put("xData",dateList);
        videoViewMap.put("yData",videoViewList);
        courseMap.put("xData",dateList);
        courseMap.put("yData",courseList);

        map.put("registerNum", registerMap);
        map.put("loginNum", loginMap);
        map.put("videoViewNum", videoViewMap);
        map.put("courseNum", courseMap);

        return map;
    }
}
