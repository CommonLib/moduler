package com.reference.apublic.web;

import android.databinding.ViewDataBinding;

import com.architecture.extend.baselib.mvvm.BaseActivity;


/**
 * Created by byang059 on 9/15/17.
 */

public class WebActivity extends BaseActivity<WebViewModel> {

    @Override
    protected void initData() {
    }

    @Override
    protected void initView(ViewDataBinding binding) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.web_activity_main;
    }
}
