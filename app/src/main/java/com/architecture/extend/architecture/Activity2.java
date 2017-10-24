package com.architecture.extend.architecture;

import android.databinding.ViewDataBinding;

import com.architecture.extend.architecture.databinding.Activity1Binding;
import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.mvvm.LiveCallBack;
import com.architecture.extend.baselib.mvvm.LiveData;

/**
 * Created by byang059 on 10/24/17.
 */

public class Activity2 extends BaseActivity<ViewModel1> {

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(ViewDataBinding binding) {
        final Activity1Binding actBinding = (Activity1Binding) binding;
        actBinding.title1.setText(getClass().getSimpleName());
        ((LiveData<String>) getSharedData("share")).subscribe(this, new LiveCallBack<String>() {
            @Override
            public void onComplete(String s) {
                actBinding.tv1.setText(s);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_1;
    }
}
