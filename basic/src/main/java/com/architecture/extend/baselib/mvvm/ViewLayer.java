package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by byang059 on 9/20/17.
 */

public interface ViewLayer {

    boolean isForeground();

    void setForegroundSwitchCallBack(ViewForegroundSwitchListener listener);

    boolean isDestroyed();

    BaseViewModel getViewModel();

    Lifecycle getLifecycle();

    LifecycleOwner getLifecycleOwner();

    void initData();

    void initView(ViewDataBinding dataBinding);

    void attachContentView(ViewGroup viewGroup, View content);

    BaseActivity getBindActivity();

    ConfigureInfo getConfigureInfo();

    ViewDelegate getViewDelegate();
}
