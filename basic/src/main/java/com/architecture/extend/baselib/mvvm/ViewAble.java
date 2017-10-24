package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.Lifecycle;

/**
 * Created by byang059 on 9/20/17.
 */

public interface ViewAble {
    boolean isForeground();
    void setForegroundSwitchCallBack(ViewForegroundSwitchListener listener);
    boolean isDestroyed();
    BaseViewModel getViewModel();
    Lifecycle getLifecycle();
}
