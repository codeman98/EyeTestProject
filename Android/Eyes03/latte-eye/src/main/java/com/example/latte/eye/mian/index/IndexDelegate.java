package com.example.latte.eye.mian.index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.latte.eye.R;
import com.example.latte.eye.R2;
import com.example.latte.eye.mian.TestEyes.TestEyeActivity;
import com.example.latte_core.delegates.bottom.BottomItemDelegate;

import butterknife.OnClick;

public class IndexDelegate extends BottomItemDelegate {

    @OnClick(R2.id.tab_eye_start_bt)
    void inTest(){
        Intent intent =new Intent(getActivity(), TestEyeActivity.class);
        startActivity(intent);
//        Toast.makeText(getContext(), "启动结束，用户登录了", Toast.LENGTH_LONG).show();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }
}
