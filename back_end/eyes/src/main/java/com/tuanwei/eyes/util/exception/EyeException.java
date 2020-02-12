package com.tuanwei.eyes.util.exception;




import com.tuanwei.eyes.util.message.ErrorCodeAndMsg;

import java.io.Serializable;



/**

 * 统一异常捕获类

 */

public class EyeException extends RuntimeException{



    private static final long serialVersionUID = -6370612186038915645L;



    private final ErrorCodeAndMsg response;



    public EyeException(ErrorCodeAndMsg response) {

        this.response = response;

    }

    public ErrorCodeAndMsg getResponse() {

        return response;

    }

}
