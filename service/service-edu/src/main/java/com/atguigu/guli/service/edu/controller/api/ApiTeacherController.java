package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
//解决浏览器同源问题

@RestController
@Api(tags = "用户讲师列表")
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("列出所有讲师")
    @GetMapping("list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list).message("获取讲师列表成功");
    }

    @ApiOperation("根据讲师id 获取讲师及其授课信息")
    @GetMapping("getById/{id}")
    public R getById(@PathVariable String id) {
        Map<String, Object> map = teacherService.selectTeacherInfoById(id);

        return R.ok().data("teacher",map.get("teacher")).data("courseList",map.get("courseList"));
    }
}

