package com.tuanwei.eyes.util.exception;



import com.tuanwei.eyes.util.message.ErrorCodeAndMsg;
import com.tuanwei.eyes.util.message.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ResponseBody;



import javax.servlet.http.HttpServletRequest;



/**

 * 创建异常处理的全局配置类

 */

@ControllerAdvice

@Slf4j

public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EyeException.class)

    @ResponseBody

    public Response handleStudentException(HttpServletRequest request, EyeException ex) {

        Response response;

        log.error("EyeException code:{},msg:{}",ex.getResponse().getCode(),ex.getResponse().getMsg());

        response = new Response(ex.getResponse().getCode(),ex.getResponse().getMsg());

        return response;

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(java.lang.Exception.class)

    @ResponseBody

    public Response handleException(HttpServletRequest request, java.lang.Exception ex) {

        Response response;

        log.error("exception error:{}",ex);

        response = new Response(ErrorCodeAndMsg.Network_error.getCode(),

                ErrorCodeAndMsg.Network_error.getMsg());

        return response;

    }

}
