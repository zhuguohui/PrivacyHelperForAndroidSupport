package com.trs.app.privacy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.trs.app.privacy.ui.UserProtocolUtil;

public abstract class CheckActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        onSetUnCheck();
        UserProtocolUtil.show(this, getAppName(), () -> {
            //用户协议
           // WebActivity.open(this,YHXY,"用户协议");
            openUserAgreement();
        }, () -> {
            //隐私政策
           // WebActivity.open(this,YSZC,"隐私政策");
            openPrivacyPolicy();
        }, () -> {
            //用户同意以后
            CheckApp.getApp().agree(this, true, getIntent() == null ? null : getIntent().getExtras());
        });
    }

    /**
     * 如果需要用到自己的界面，需要重写该方法。
     */
    protected void onSetUnCheck(){
        CheckApp.getApp().uncheck(WebActivity.class);
    }

    protected String getAppName(){
        return getMyAppName(this);
    }

    private  String getMyAppName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {
            Log.i("zzz","getAppName >> e:" + e.toString());
        }
        return "demo";
    }


    /**
     * 提供一个打开url的简便方法
     * @param url
     * @param title
     */
    protected void openUrl(String url,String title){
        WebActivity.open(this,url,title);
    }

    /**
     * 重写用于打开隐私政策
     */
    protected abstract void openPrivacyPolicy();


    /**
     * 重写用于打开用户协议
     */
    protected abstract void openUserAgreement();

}