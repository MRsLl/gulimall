package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@RestController
@Api(tags = "用户端课程展示")
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private ChapterService chapterService;

    @ApiOperation("订单支付完成后，更改课程销量")
    @GetMapping("updateCourseBuyCount/{courseId}")
    public R updateCourseBuyCount(@PathVariable String courseId){
        Course course = courseService.getById(courseId);
        course.setBuyCount(course.getBuyCount() + 1);

        courseService.updateById(course);
        return R.ok();
    }

    @ApiOperation("根据课程id 查询订单中课程信息")
    @GetMapping("getCourseDtoById/{courseId}")
    public R getCourseDtoById(@PathVariable String courseId) {
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return R.ok().data("courseDto",courseDto);
    }

    @ApiOperation("根据课程id 查询用户端显示的课程信息")
    @GetMapping("getById/{id}")
    public R getById(@PathVariable String id){
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(id);

        List<ChapterVo> chapterList = chapterService.getChapterList(id);

        return R.ok().data("course",webCourseVo).data("chapterList",chapterList);
    }

    @ApiOperation("根据用户筛选条件查询课程集合")
    @GetMapping("list")
    public R list(WebCourseQuery webCourseQuery){
        List<Course> courseList = courseService.selectWebCourse(webCourseQuery);
        return R.ok().data("items",courseList);
    }
}

