package com.architecture.extend.baselib.mvvm;

import android.support.annotation.CallSuper;

import com.architecture.extend.baselib.BaseApplication;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.android.InjectAble;
import dagger.android.ObjectInjection;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository implements InjectAble {

    @Inject
    Executor mExecutor;

    public BaseRepository() {
        onCreate();
    }

    @CallSuper
    public void onCreate() {
        ObjectInjection.inject(this, BaseApplication.getInstance());
    }

    protected void runOnWorkerThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
