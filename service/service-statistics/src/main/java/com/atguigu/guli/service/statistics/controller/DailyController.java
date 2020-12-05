package com.atguigu.guli.service.statistics.controller;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-04
 */
@Api(tags = "每日数据统计管理")
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation("生成每天的统计记录")
    @PostMapping("create/{day}")
    public R createStatisticsByDay(@PathVariable String day) throws ParseException {
        dailyService.createStatisticsByDay(day);
        return R.ok().message("数据统计成功");
    }

    @ApiOperation("显示统计数据")
    @GetMapping("showChart/{begin}/{end}")
    public R showChart(@PathVariable String begin,
                       @PathVariable String end){
        Map<String,Map> chartMap = dailyService.showChart(begin,end);

        return R.ok().data("chartData",chartMap);
    }
}

