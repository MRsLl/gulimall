package com.atguigu.guli.service.ucenter.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class DateTest {

    @Test
    public void testDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = dateFormat.parse("20190102");
        String format = dateFormat.format(date);

        System.out.println(format);
        System.out.println(date.toString());

    }

}
