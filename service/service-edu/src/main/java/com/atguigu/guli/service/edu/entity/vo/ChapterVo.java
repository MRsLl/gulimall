package com.atguigu.guli.service.edu.entity.vo;

import com.atguigu.guli.service.edu.entity.Video;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChapterVo {

    private String id;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "章节名称")
    private String title;

    @ApiModelProperty(value = "显示排序")
    private Integer sort;

    @ApiModelProperty(value = "课时集合")
    private List<Video> children;
}
