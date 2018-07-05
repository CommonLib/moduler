package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.support.annotation.CallSuper;

import com.architecture.extend.baselib.dagger.InjectionUtil;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository {

    @Inject
    Executor mExecutor;

    public BaseRepository(Activity activity) {
        InjectionUtil.maybeInject(activity, this);
        onCreate();
    }

    @CallSuper
    public void onCreate() {
    }

    protected void runOnWorkerThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
