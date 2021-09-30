package com.trs.app.demo;

import com.trs.app.privacy.CheckActivity;

public class MyCheckActivity extends CheckActivity {


    @Override
    protected void openPrivacyPolicy() {
        openUrl("http://www.baidu.com","隐私政策");
    }

    @Override
    protected void openUserAgreement() {
        openUrl("http://www.zhihu.com","用户协议");
    }
}