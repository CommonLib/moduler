package com.architecture.extend.baselib.mvvm;

import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;

import com.architecture.extend.baselib.dagger.InjectionUtil;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository extends Fragment{

    @Inject
    Executor mExecutor;

    public BaseRepository() {
        InjectionUtil.maybeInject(this);
        onCreate();
    }

    @CallSuper
    public void onCreate() {
    }

    protected void runOnWorkerThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
