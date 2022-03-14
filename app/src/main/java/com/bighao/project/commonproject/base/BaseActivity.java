package com.bighao.project.commonproject.base;

import android.content.res.Resources;
import android.view.View;

import com.bighao.project.common.ui.CommonActivity;

import butterknife.ButterKnife;

/**
 * Created by Hao on 2022/3/14
 */
public abstract class BaseActivity extends CommonActivity {

    @Override
    public void setContentView(View view) {
        ButterKnife.bind(this, view);
        super.setContentView(view);
    }

    @Override
    protected void initView(View view) {

    }

    /**
     * 禁止app字体大小跟随系统字体大小调节
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }
}
