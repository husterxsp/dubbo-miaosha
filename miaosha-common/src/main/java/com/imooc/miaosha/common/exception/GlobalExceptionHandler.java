package com.imooc.miaosha.common.exception;

import com.imooc.miaosha.common.result.CodeMsg;
import com.imooc.miaosha.common.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xushaopeng
 * @date 2018/10/02
 * 异常拦截器，可以让错误信息显示更友好
 * 没有这个，Validator 验证不通过会显示一大串错误信息
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    // 这里写成 Exception.class 相当于拦截了 所有的异常。
    // 这里方法的参数和controller一样。
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {

        e.printStackTrace();

        if (e instanceof GlobalException) {
            // 如果是自定义的GlobalException
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        } else if (e instanceof BindException) {
            // 如果是绑定异常
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

    }
}
