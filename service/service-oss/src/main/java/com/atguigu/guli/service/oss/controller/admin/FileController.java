package com.atguigu.guli.service.oss.controller.admin;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "阿里云文件管理")
@Slf4j

@RestController
@RequestMapping("/admin/oss/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R fileUpload(@RequestParam(value = "module") String module, MultipartFile file){

        String fileUrl = null;
        try {
            fileUrl = fileService.fileUpload(module, file);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
        return R.ok().data("fileUrl",fileUrl);
    }

    @ApiOperation("删除文件")
    @DeleteMapping("remove")
    public R removeFile(@RequestBody String url) {
        fileService.removeFile(url);
        return R.ok().message("文件删除成功");
    }

    @ApiOperation("批量删除文件")
    @DeleteMapping("batchRemove")
    public R batchRemoveFile(@RequestBody List<String> urls) {
        fileService.batchRemove(urls);
        return R.ok().message("文件批量删除成功");
    }
}
