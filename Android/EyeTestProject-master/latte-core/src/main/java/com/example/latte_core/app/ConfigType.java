package com.example.latte_core.app;

/**
 * 枚举类
 */
public enum ConfigType {
    API_HOST,  //域名
    APPLICATION_CONTEXT, //全局上下文
    CONFIG_READY,  //初始化或配置完成没
    LOADER_DELAYED,
    ICON ,         //字体初始化项目
    INTERCEPTOR,
    WE_CHAT_APP_ID,
    WE_CHAT_APP_SECRET,
    ACTIVITY,
    HANDLER,
    JAVASCRIPT_INTERFACE
}
