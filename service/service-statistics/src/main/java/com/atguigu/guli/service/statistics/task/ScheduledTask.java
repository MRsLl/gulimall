package com.atguigu.guli.service.statistics.task;

import com.atguigu.guli.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    private DailyService dailyService;

    @Scheduled(cron = "0 0 1 * * *")
    public void taskGenarateStatisticsData(){
        //获取上一天的日期
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createStatisticsByDay(day);
        log.info("taskGenarateStatisticsData 统计完毕");
    }
}