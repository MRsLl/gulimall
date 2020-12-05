package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.feign.VodMediaService;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@Api(tags = "课时管理")

@RestController
@RequestMapping("/admin/edu/video")
public class AdminVideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("根据id 删除课时")
    @DeleteMapping("removeVideoById/{id}")
    public R removeVideoById(@PathVariable String id){

        boolean b = videoService.removeVideoById(id);

        if (b) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("根据id 获取课时")
    @GetMapping("getVideoById/{videoId}")
    public R getVideoById(@PathVariable String videoId){
        Video video = videoService.getById(videoId);
        return R.ok().data("item",video);
    }

    @ApiOperation("修改课时信息")
    @PutMapping("updateVideo")
    public R updateVideo(@RequestBody Video video){
        boolean b = videoService.updateById(video);
        if (b) {
            return R.ok().message("更新成功");
        } else {
            return R.error().message("更新失败");
        }
    }

    @ApiOperation("添加课时")
    @PostMapping("saveVideo")
    public R saveVideo(@RequestBody Video video){
        boolean b = videoService.save(video);
        if (b) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

}

