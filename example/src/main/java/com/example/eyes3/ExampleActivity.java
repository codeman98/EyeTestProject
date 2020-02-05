package com.example.eyes3;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.example.latte.eye.launcher.LauncherDelegate;
import com.example.latte.eye.mian.EyeBottomDelegate;
import com.example.latte.eye.mian.index.IndexDelegate;
import com.example.latte.eye.sign.ISignListener;
import com.example.latte.eye.sign.SignInDelegate;
import com.example.latte_core.activities.ProxyActivity;
import com.example.latte_core.delegates.LatteDelegate;
import com.example.latte_ui.launcher.ILauncherListener;
import com.example.latte_ui.launcher.OnLauncherFinishTag;

import java.io.BufferedReader;


public class ExampleActivity extends ProxyActivity implements ISignListener, ILauncherListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        final ActionBar actionBar = getSupportActionBar();//[去除titleBar]
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new LauncherDelegate();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag) {
            case SIGNED:
//                Toast.makeText(this, "启动结束，用户登录了", Toast.LENGTH_LONG).show();
                getSupportDelegate().startWithPop(new EyeBottomDelegate());
                break;
            case NOT_SIGNED:
//                Toast.makeText(this, "启动结束，用户没登录", Toast.LENGTH_LONG).show();
                getSupportDelegate().startWithPop(new SignInDelegate());
                break;
            default:
                break;
        }
    }

}
