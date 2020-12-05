package com.atguigu.guli.service.ucenter.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户登录表单对象")
public class LoginForm {

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;
}