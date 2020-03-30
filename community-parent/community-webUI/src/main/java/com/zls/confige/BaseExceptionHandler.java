package com.zls.confige;

import entity.Result;
import entity.StatusCode;
import enums.CustomizeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 */
@ControllerAdvice
@CrossOrigin
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        if (e instanceof CustomizeException){
            CustomizeException exception = (CustomizeException) e;
            return new Result(exception.getFlag(), exception.getCode(), exception.getMessage());
        }
        return new Result(false, StatusCode.ERROR, e.getMessage(),e.getMessage());
    }

}
