package com.example.latte_core.app;

/**
 * 用户状态的回调和用户信息的回调
 */
public interface IUserChecker {

    void onSignIn();//有用户信息

    void onNotSignIn();//没有用户信息
}
