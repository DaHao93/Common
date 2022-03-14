package com.bighao.project.common.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bighao.project.common.R;

/**
 * Created by Hao on 2022/3/14
 */
public abstract class CommonActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        View commonView = LayoutInflater.from(this).inflate(R.layout.activity_common, null);
        RelativeLayout container = commonView.findViewById(R.id.rl_common_content_container);

        if (null != view) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.removeAllViews();
            container.addView(view, params);
            initView(view);
        }
        getDelegate().setContentView(commonView);
    }

    protected abstract void initView(View view);

}
