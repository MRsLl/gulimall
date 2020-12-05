package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.logging.Logger;
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
@Api(tags = "章节管理")

@RestController
@RequestMapping("/admin/edu/chapter")
public class AdminChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private VideoService videoService;

    @ApiOperation("根据id 删除章节及所有课时")
    @DeleteMapping("removeById/{id}")
    public R removeById(@PathVariable String id){
        //根据chapterId 从 vod 中删除视频
        videoService.removeMediaVideoByChapterId(id);

        boolean b = chapterService.removeChapterById(id);

        if (b) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("更新章节")
    @PutMapping("updateChapter")
    public R updateChapter(@RequestBody Chapter chapter){
        boolean b = chapterService.updateById(chapter);
        if (b) {
            return R.ok().message("更新成功");
        } else {
            return R.error().message("更新失败");
        }
    }

    @ApiOperation("根据id 获取章节")
    @GetMapping("getChapterById/{id}")
    public R getChapterById(@PathVariable String id){
        Chapter chapter = chapterService.getById(id);
        if (chapter != null){
            return R.ok().data("item",chapter);
        }
        return R.error().message("没有那个章节");
    }

    @ApiOperation("查询嵌套章节信息")
    @GetMapping("getChapterList/{courseId}")
    public R getChapterList(@PathVariable String courseId){
        List<ChapterVo> chapterList = chapterService.getChapterList(courseId);
        return R.ok().data("items",chapterList);
    }

    @ApiOperation("保存章节信息")
    @PostMapping("saveChapter")
    public R saveChapter(@RequestBody Chapter chapter){
        boolean save = chapterService.save(chapter);

        if (save) {
            return R.ok().message("保存章节成功");
        } else {
            return R.error().message("保存章节失败");
        }
    }
}

