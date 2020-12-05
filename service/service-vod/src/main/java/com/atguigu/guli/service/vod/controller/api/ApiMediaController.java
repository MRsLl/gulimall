package com.atguigu.guli.service.vod.controller.api;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.service.MediaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "用户 vod 视频管理")
@Slf4j
@RestController

@RequestMapping("/api/vod/media")
public class ApiMediaController {

    @Autowired
    private MediaService mediaService;

    @ApiOperation("根据id 获取视频播放地址")
    @GetMapping("getPlayUrl/{id}")
    public R getPlayUrl(@PathVariable String id){
       String playUrl = mediaService.getPlayUrl(id);
       return R.ok().data("playUrl",playUrl);
    }

    @ApiOperation("根据id 获取视频播放凭证")
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        String playAuth = mediaService.getPlayAuth(id);
        return R.ok().data("playAuth",playAuth);
    }
}
