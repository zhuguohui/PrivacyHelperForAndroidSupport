package com.trs.app.demo;

import android.widget.Toast;

import com.trs.app.privacy.CheckApp;

/**
 * Created by zhuguohui
 * Date: 2021/9/30
 * Time: 10:52
 * Desc:
 */
public class MyApp extends CheckApp {

    @Override
    protected void initSafeSDK() {

        Toast.makeText(this,"不需要权限的sdk初始化成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initSDK() {
        Toast.makeText(this,"需要敏感权限的sdk初始化成功",Toast.LENGTH_SHORT).show();
    }
}
