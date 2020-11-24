package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * 全局异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        log.error(e.getMessage());
        return R.error();
    }

    @ResponseBody
    @ExceptionHandler(BadSqlGrammarException.class)
    public R error(BadSqlGrammarException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ResponseBody
    @ExceptionHandler(GuliException.class)
    public R error(GuliException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }

    @ResponseBody
    @ExceptionHandler( DataIntegrityViolationException.class)
    public R error( DataIntegrityViolationException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.DATA_INTEGRITY_VIOLATION_ERROR);
    }


}
