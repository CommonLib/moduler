package com.architecture.extend.baselib.mvvm;

import android.support.annotation.CallSuper;

import com.architecture.extend.baselib.dagger.Injector;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.android.AndroidInjector;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository {

    @Inject
    Executor mExecutor;

    public BaseRepository(BaseActivity activity) {
        AndroidInjector injector = activity.getInjector();
        if(injector instanceof Injector){
            ((Injector) injector).injectRepository(this);
        }
        onCreate();
    }

    @CallSuper
    public void onCreate() {
    }

    protected void runOnWorkerThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
