package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EduCourseServiceFallBack implements EduCourseService {
    @Override
    public R getCourseDtoById(String courseId) {
        log.warn("熔断保护");
        return R.error();
    }

    @Override
    public R updateCourseBuyCount(String courseId) {
        log.warn("熔断保护");
        return R.error();
    }
}