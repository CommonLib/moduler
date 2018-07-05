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
    protected void initData() {
        if (mView != null) {
            LogUtil.d("SecondActivity inject success" + this);
            LogUtil.d("SecondActivity inject success" + mView);
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
