package com.atguigu.guli.service.edu.controller.admin;


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
@Api(tags = "讲师管理")
@RequestMapping("/admin/edu/teacher")
public class AdminTeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("列出所有讲师")
    @GetMapping("list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list).message("获取讲师列表成功");
    }

    @ApiOperation(value = "删除讲师", notes = "根据id删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(@PathVariable String id) {
        //删除头像
        teacherService.removeAvatarById(id);
        //删除讲师
        boolean b = teacherService.removeById(id);

        if (b) {
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }

    @ApiOperation("按条件分页查询讲师列表")
    @GetMapping("list/{page}/{pageSize}")
    public R listPage(@PathVariable Long page,
                      @PathVariable Long pageSize,
                      @ApiParam("讲师查询vo条件对象") TeacherQuery teacherQuery) {
        IPage<Teacher> teacherIPage = teacherService.selectPage(page, pageSize, teacherQuery);

        return R.ok().message("page 分页查询").data("page", teacherIPage);
    }

    @ApiOperation("保存讲师")
    @PostMapping("save")
    public R save(@RequestBody @ApiParam(value = "讲师对象", required = true) Teacher teacher) {
        boolean save = teacherService.save(teacher);
        if (save) {
            return R.ok().message("插入成功");
        }
        return R.error().message("插入失败");
    }

    @ApiOperation("修改讲师")
    @PutMapping("update")
    public R update(@RequestBody @ApiParam(value = "讲师对象", required = true) Teacher teacher) {
        boolean update = teacherService.updateById(teacher);
        if (update) {
            return R.ok().message("修改成功");
        }
        return R.error().message("修改失败");
    }

    @ApiOperation("根据讲师id 查询")
    @GetMapping("get/{id}")
    public R getTeacherById(@PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }

    @ApiOperation("根据id 批量删除讲师")
    @DeleteMapping("batchRemove")
    public R batchRemove(@RequestBody List<String> ids) {
        //批量删除讲师头像
        teacherService.batchRemoveAvatarsByIds(ids);
        //批量删除讲师
        boolean b = teacherService.removeByIds(ids);
        if (b) {
            return R.ok().message("批量删除成功");
        } else {
            return R.error().message("批量删除失败");
        }
    }

    @ApiOperation("根据左关键字查询讲师名列表")
    @GetMapping("list/name/{key}")
    public R selectNameListByKey(@PathVariable String key){
        List<Map<String,Object>> nameList = teacherService.selectNameListByKey(key);
        return R.ok().data("nameList",nameList);
    }

}

