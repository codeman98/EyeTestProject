package com.example.eyes3;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.latte_core.delegates.LatteDelegate;
import com.example.latte_core.net.RestClient;
import com.example.latte_core.net.callback.IError;
import com.example.latte_core.net.callback.IFailure;
import com.example.latte_core.net.callback.ISuccess;


public class ExampleDelegate extends LatteDelegate {

    @Override
    public Object setLayout() {
        return com.example.eyes3.R.layout.delegate_example;
    }



    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
//        testRestClient();
    }


    private void testRestClient(){
        RestClient.builder()
                .url("http://127.0.0.1/test")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("你哈",response);
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build()
                .get();
    }
}


