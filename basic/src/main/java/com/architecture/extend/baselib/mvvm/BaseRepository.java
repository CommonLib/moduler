package com.architecture.extend.baselib.mvvm;

import android.support.annotation.CallSuper;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository{

    @Inject
    Executor mExecutor;

    @CallSuper
    public void onCreate() {
    }

    protected void runOnWorkerThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
