package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.CourseDescription;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.service.CourseDescriptionService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
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
@Api(tags = "课程管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/course")
public class AdminCourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseDescriptionService courseDescriptionService;

    @ApiOperation("根据id 发布课程")
    @PutMapping("publishCourseById/{id}")
    public R publishCourseById(@PathVariable String id){
        boolean b = courseService.publishCourseById(id);
        if (b) {
            return R.ok().message("发布成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据 id 获取要发布的课程信息")
    @GetMapping("getCoursePublishById/{id}")
    public R getCoursePublishById(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishById(id);
        return R.ok().data("item",coursePublishVo);
    }

    @ApiOperation("根据课程id 删除课程")
    @DeleteMapping("removeById/{id}")
    public R removeById(@PathVariable String id){
        //vod 删除视频
        courseService.removeVideoById(id);
        //oss 删除封面图片
        courseService.removeCoverById(id);

        boolean b = courseService.removeCourseById(id);

        if (b) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("没有这个课程");
        }
    }

    @ApiOperation("按条件分页查询课程集合")
    @GetMapping("listCourseByPage/{page}/{limit}")
    public R listCourseByPage(@PathVariable Integer page,
                              @PathVariable Integer limit,
                              CourseQuery courseQuery){
        IPage<CourseVo> courseVoIPage = courseService.listCourseByPage(page, limit, courseQuery);

        List<CourseVo> records = courseVoIPage.getRecords();
        long total = courseVoIPage.getTotal();

        return R.ok().data("rows",records).data("total",total);
    }

    @ApiOperation("更新课程信息")
    @PutMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        //更新课程
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm,course);
        courseService.updateById(course);

        //更新课程描述
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionService.updateById(courseDescription);

        return R.ok().message("更新成功");
    }
    @ApiOperation("根据id 获取课程")
    @GetMapping("getCourseInfoById/{id}")
    public R getCourseInfoById(@PathVariable String id){
        Course course = courseService.getById(id);
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course,courseInfoForm);

        CourseDescription courseDescription = courseDescriptionService.getById(id);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return R.ok().data("item",courseInfoForm);
    }

    @ApiOperation("保存课程信息")
    @PostMapping("saveCourseInfo")
    public R saveCourseInfo(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId).message("保存成功");
    }
}

