package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.Lifecycle;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by byang059 on 9/20/17.
 */

public interface Viewable {
    boolean isForeground();
    void setForegroundSwitchCallBack(ViewForegroundSwitchListener listener);
    boolean isDestroyed();
    BaseViewModel getViewModel();
    Lifecycle getLifecycle();
    RxPermissions getRxPermissions();
}
