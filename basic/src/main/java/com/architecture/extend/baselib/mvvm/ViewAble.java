package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 9/20/17.
 */

public interface ViewAble {
    boolean isForeground();
    void setForegroundSwitchCallBack(ViewForegroundSwitchListener listener);
}
