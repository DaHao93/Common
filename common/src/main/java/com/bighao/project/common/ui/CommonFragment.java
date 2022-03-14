package com.bighao.project.common.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bighao.project.common.R;

/**
 * Created by Hao on 2022/3/14
 */
public abstract class CommonFragment extends Fragment {

    protected Context mContext;
    private RelativeLayout mContainer;
    protected boolean isLoadData;

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_common, null);

        mContainer = rootView.findViewById(R.id.rl_common_content_container);

        View contentView = getLayoutInflater().inflate(onCreateLayout(inflater, savedInstanceState), null);

        setContentView(contentView);

        initView(contentView);

        return rootView;
    }

    protected void setContentView(View view) {
        if (null != view) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mContainer.removeAllViews();
            mContainer.addView(view, params);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoadData) {
            loadData();
        }
    }

    /**
     * 用于请求数据，当isLoadData==ture的时候不会再走这个方法
     * 请使用viewpager2   设置setOffscreenPageLimit
     */
    protected abstract void loadData();

    protected abstract int onCreateLayout(@NonNull LayoutInflater inflater, @Nullable Bundle savedInstanceState);

    /**
     * 用于页面初始化
     *
     * @param view
     */
    protected abstract void initView(View view);
}
