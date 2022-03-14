package com.bighao.project.common;

import android.app.Application;

import com.bighao.project.common.content.SharedPreference;
import com.bighao.project.common.uitls.AppUtils;

/**
 * Created by Hao on 2022/3/14
 */
public abstract class CommonApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtils.init(this);
        SharedPreference.init(this);
    }
}
