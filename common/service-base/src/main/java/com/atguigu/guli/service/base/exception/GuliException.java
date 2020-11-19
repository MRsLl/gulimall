package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.Data;

@Data
public class GuliException extends RuntimeException{
    private Integer code;

    /**
     * 接受状态码和消息
     * @param code
     * @param message
     */
    public GuliException(Integer code, String message) {
        super(message);
        this.code=code;
    }

    public GuliException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }


}
