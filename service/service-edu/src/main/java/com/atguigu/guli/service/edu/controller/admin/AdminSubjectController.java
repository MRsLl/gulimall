package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@CrossOrigin
@Api(tags = "课程类别管理")
@RestController
@RequestMapping("/admin/edu/subject")
public class AdminSubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "Excel批量导入课程类别数据")
    @PostMapping("batchImport")
    public R batchImport(
            @ApiParam(value = "Excel文件", required = true)
            @RequestParam("file") MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }


    @ApiOperation(value = "嵌套查询课程分类列表")
    @GetMapping("nestedList")
    public R nestedList(){
        List<SubjectVo> subjectVos = subjectService.nestedList();
        return R.ok().data("items",subjectVos);
    }
}

