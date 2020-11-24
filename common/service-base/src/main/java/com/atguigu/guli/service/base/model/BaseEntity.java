package com.atguigu.guli.service.base.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
//指定setter 方法为链式调用
@Accessors(chain = true)
public class BaseEntity {

    @ApiModelProperty(value = "ID")
    //默认使用雪花算法生成id
    private String id;

    @ApiModelProperty(value = "创建时间")
    //插入数据时自动填充该字段
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
