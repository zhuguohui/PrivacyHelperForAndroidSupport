package com.trs.app.privacy;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

/**
 * Created by zhuguohui
 * Date: 2021/7/30
 * Time: 13:46
 * Desc:
 */
public class ApplicationInstrumentation extends Instrumentation {

    private static final String TAG = "ApplicationInstrumentation";

    // ActivityThread中原始的对象, 保存起来
    Instrumentation mBase;

    public ApplicationInstrumentation(Instrumentation base) {
        mBase = base;
    }

    public Activity newActivity(ClassLoader cl, String className,
                                Intent intent)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        className = CheckApp.getApp().getActivityName(className);
        return mBase.newActivity(cl, className, intent);
    }


}

