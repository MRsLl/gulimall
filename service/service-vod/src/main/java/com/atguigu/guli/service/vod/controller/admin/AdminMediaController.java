package com.atguigu.guli.service.vod.controller.admin;

import com.aliyuncs.exceptions.ClientException;
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

@Api(tags = "vod 视频管理")
@Slf4j
@RestController

@RequestMapping("/admin/vod/media")
public class AdminMediaController {

    @Autowired
    private MediaService mediaService;

    @ApiOperation("根据视频id 集合 从 vod 删除视频")
    @DeleteMapping("removeVideoByList")
    public R removeVideoByList(@RequestBody List<String> videoSourceIdList) {
        try {
            mediaService.removeVideoByList(videoSourceIdList);
            return R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @ApiOperation("根据视频id 从 vod 删除视频")
    @DeleteMapping("delete/{videoSourceId}")
    public R delete(@PathVariable String videoSourceId) {
        mediaService.delete(videoSourceId);
        return R.ok().message("视频删除成功");
    }

    @ApiOperation("上传视频到 vod")
    @PostMapping("upload")
    public R upload(@RequestBody MultipartFile file) {
        String videoId = mediaService.upload(file);
        return R.ok().data("videoId",videoId);
    }

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
