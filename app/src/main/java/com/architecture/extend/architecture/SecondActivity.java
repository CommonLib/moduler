package com.architecture.extend.architecture;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;

/**
 * Created by byang059 on 10/24/17.
 */
public class SecondActivity extends BaseActivity<SecondViewModel> {

    @Inject
    View mView;

    @Override
    public void initData() {
        if (mView != null) {
            LogUtil.d("SecondActivity inject success" + this);
            System.out.println(mView);
        }
    }

    @Override
    public void initView(ViewDataBinding dataBinding) {

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
