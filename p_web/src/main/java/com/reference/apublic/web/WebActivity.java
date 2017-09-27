package com.reference.apublic.web;

import android.databinding.ViewDataBinding;
import android.widget.Toast;

import com.architecture.extend.baselib.mvvm.BaseActivity;


/**
 * Created by byang059 on 9/15/17.
 */

public class WebActivity extends BaseActivity<WebViewModel> {

    @Override
    protected void initData() {
        String abc = (String) getSharedData("abc");
        Toast.makeText(this, abc, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void initView(ViewDataBinding binding) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.web_activity_main;
    }
}
