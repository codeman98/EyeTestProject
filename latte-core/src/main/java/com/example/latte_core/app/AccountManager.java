package com.example.latte_core.app;

import com.example.latte_core.util.storage.LattePreference;

/**
 * 用户信息管理
 */
public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }

    //保存用户登录状态，登录后调用
    public static void setSignState(boolean state) {
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }
//判断是否已经登录
    private static boolean isSignIn() {
        return LattePreference.getAppFlag(SignTag.SIGN_TAG.name());
    }
//检查是否已经登录
    public static void checkAccount(IUserChecker checker) {
        if (isSignIn()) {
            checker.onSignIn();
        } else {
            checker.onNotSignIn();
        }
    }
}
