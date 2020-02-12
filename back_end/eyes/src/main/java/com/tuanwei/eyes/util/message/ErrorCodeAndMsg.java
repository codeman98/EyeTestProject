package com.tuanwei.eyes.util.message;


/**

 * 错误消息

 */

public enum  ErrorCodeAndMsg {



    EmailAndPassword_Error("0001","账号密码错误"),

    User_exist("0002","用户已经存在"),

    Network_error("9999","网络错误，待会重试"),

    ;



    private String code;

    private String msg;



    ErrorCodeAndMsg(String code, String msg) {

        this.code = code;

        this.msg = msg;

    }



    public String getCode() {

        return code;

    }

    public void setCode(String code) {

        this.code = code;

    }

    public String getMsg() {

        return msg;

    }

    public void setMsg(String msg) {

        this.msg = msg;

    }

}
