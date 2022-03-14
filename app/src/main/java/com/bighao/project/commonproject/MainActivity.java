package com.bighao.project.commonproject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bighao.project.commonproject.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    TextView mContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mContentTv.setText("你好啊");
    }
}