package com.example.latte.eye.sign;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.eye.database.DatabaseManager;
import com.example.latte.eye.database.Users;
import com.example.latte_core.app.AccountManager;

/**
 * 数据持久化
 */
public class SignHandler {

    public static void onSignIn(String response, ISignListener signListener) {
//        final String codeJson = JSON.parseObject(response).getString("code");
//        System.out.println(codeJson);
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long id = profileJson.getLong("id");
        final String username = profileJson.getString("username");
        final String password = profileJson.getString("password");
        final String email = profileJson.getString("email");
        final String phone = profileJson.getString("phone");

        final Users profile = new Users(id, username, password, email, phone);
        DatabaseManager.getInstance().getDao().insert(profile);

        //已经注册并登录成功了
        AccountManager.setSignState(true);
        signListener.onSignInSuccess();
    }


    public static void onSignUp(String response, ISignListener signListener) {
        final String code = JSON.parseObject(response).getString("code");
        if(code.equals("0000")) {
            final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
            final long id = profileJson.getLong("id");
            final String username = profileJson.getString("username");
            final String password = profileJson.getString("password");
            final String email = profileJson.getString("email");
            final String phone = profileJson.getString("phone");

            final Users profile = new Users(id, username, password, email, phone);
            DatabaseManager.getInstance().getDao().insert(profile);

            //已经注册并登录成功了
            AccountManager.setSignState(true);
            signListener.onSignUpSuccess();
        }
    }
}