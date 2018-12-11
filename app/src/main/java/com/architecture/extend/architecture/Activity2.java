package com.architecture.extend.architecture;

import android.databinding.ViewDataBinding;

import com.architecture.extend.baselib.mvvm.BaseActivity;

/**
 * Created by byang059 on 10/24/17.
 */

public class Activity2 extends BaseActivity<SecondViewModel> {

    @Override
    public void initData() {

    }

    @Override
    public void initView(ViewDataBinding binding) {

    }

    @Override
    public Class<SecondViewModel> getViewModelClass() {
        return SecondViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_1;
    }
}
