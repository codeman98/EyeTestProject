package com.example.latte_core.delegates.bottom;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.latte_core.R;
import com.example.latte_core.delegates.LatteDelegate;

/**
 * BottomItemDelegate 是每一个页面
 */
public abstract class BottomItemDelegate extends LatteDelegate implements View.OnKeyListener{

    private long mExitTime = 0;
    private static final int EXIT_TIME = 2000;//退出时间容量

    // [request请求]fragment在返回的时候需要将fouse再次去request；需要读取fragment的源码；
    @Override
    public void onResume() {
        super.onResume();
        final  View rootView = getView();
        if(rootView != null){
            rootView.setFocusable(true);
            rootView.requestFocus();//请求focus
            rootView.setOnKeyListener(this);//注册listener
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {//点击两次返回按钮可以退出应用的app
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN ){
            if((System.currentTimeMillis()-mExitTime)>EXIT_TIME){
                Toast.makeText(getContext(),"双击退出"+getString(R.string.app_name),Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }else{
                _mActivity.finish();//点击很快的话退出应用
                if(mExitTime != 0){
                    mExitTime = 0;
                }
            }
        }
        return true;
    }

}
