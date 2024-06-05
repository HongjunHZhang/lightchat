package com.zhj.exception;

import com.zhj.entity.result.DataResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 789
 */
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public DataResult exceptionHandler(RuntimeException e){
        return DataResult.errorOfData(e.getMessage());
    }

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public DataResult exceptionHandler(CustomException e){
        return DataResult.errorOfData(e.getCode(), e.getMessage());
    }

}
