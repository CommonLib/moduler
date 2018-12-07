package com.reference.apublic.web;

import android.databinding.ViewDataBinding;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.module.contract.router.RouterConstants;


/**
 * Created by byang059 on 9/15/17.
 */

@Route(path = RouterConstants.Web.PAGE_WEB)
public class WebActivity extends BaseActivity<WebViewModel> {


    @Override
    public void initData() {
    }

    @Override
    public void initView(ViewDataBinding binding) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.web_activity_main;
    }
}
