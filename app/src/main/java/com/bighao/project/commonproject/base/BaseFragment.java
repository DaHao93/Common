package com.bighao.project.commonproject.base;

import android.view.View;

import com.bighao.project.common.ui.CommonFragment;

import butterknife.ButterKnife;

/**
 * Created by Hao on 2022/3/14
 */
public abstract class BaseFragment extends CommonFragment {

    @Override
    protected void setContentView(View view) {
        ButterKnife.bind(this, view);
        super.setContentView(view);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void loadData() {

    }
}
