package com.architecture.extend.architecture;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.util.LogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

/**
 * Created by byang059 on 10/24/17.
 */
public class SecondActivity extends BaseActivity<ViewModel1> {

    @Inject
    View mView;

    @Inject
    RxPermissions mRxPermissions;

    @Override
    protected void initData() {
        if (mView != null) {
            LogUtil.d("SecondActivity inject success" + this);
            LogUtil.d("SecondActivity inject success" + mView);
            LogUtil.d("SecondActivity inject success" + mRxPermissions);
        }
    }

    @Override
    protected void initView(ViewDataBinding dataBinding) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_1;
    }
}
