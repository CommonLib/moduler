package com.architecture.extend.architecture;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.architecture.extend.architecture.databinding.Activity1Binding;
import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.mvvm.LiveCallBack;
import com.architecture.extend.baselib.mvvm.LiveData;

/**
 * Created by byang059 on 10/24/17.
 */

public class Activity1 extends BaseActivity<ViewModel1> {

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(ViewDataBinding binding) {
        final Activity1Binding actBinding = (Activity1Binding) binding;
        actBinding.title1.setText(getClass().getSimpleName());
        actBinding.title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity1.this, Activity2.class));
            }
        });
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
