package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.EduCourseServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "service-edu"/*,fallback = EduCourseServiceFallBack.class*/)
public interface EduCourseService {

    @GetMapping("/api/edu/course/getCourseDtoById/{courseId}")
    R getCourseDtoById(@PathVariable String courseId);

    @GetMapping("/api/edu/course/updateCourseBuyCount/{courseId}")
    R updateCourseBuyCount(@PathVariable String courseId);

}
