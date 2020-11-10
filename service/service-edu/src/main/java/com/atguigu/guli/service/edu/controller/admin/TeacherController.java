package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/admin/edu/teacher")
@ApiModel("讲师方法")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @ApiOperation("列出所有讲师")
    @GetMapping("/list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list).message("获取讲师列表成功");
    }

    @ApiOperation(value = "删除讲师",notes = "根据id删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(@PathVariable Integer id) {
        boolean b = teacherService.removeById(id);
        if (b){
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }

    @GetMapping("list/{page}/{pageSize}")
    public R listPage(@PathVariable Long page,
                      @PathVariable Long pageSize,
                      @ApiParam("讲师查询vo条件对象") TeacherQuery teacherQuery) {
        IPage<Teacher> teacherIPage = teacherService.selectPage(page, pageSize, teacherQuery);

        return R.ok().message("page 分页查询").data("page",teacherIPage);
    }

    @PostMapping("save")
    public R save(Teacher teacher) {
        boolean save = teacherService.save(teacher);
        if (save) {
            return R.ok().message("插入成功");
        }
        return R.error().message("插入失败");
    }

    @PutMapping("update")
    public R update(Teacher teacher) {
        boolean update = teacherService.updateById(teacher);
        if (update) {
            return R.ok().message("修改成功");
        }
        return R.error().message("修改失败");
    }
}

