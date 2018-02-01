package com.architecture.extend.baselib.mvvm;

import android.os.AsyncTask;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.dagger.ApplicationAble;
import com.architecture.extend.baselib.dagger.ObjectInjection;

import java.util.concurrent.Executor;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository implements ApplicationAble {

    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public BaseRepository() {
        ObjectInjection.inject(this);
        onCreate();
    }

    public void onCreate() {

    }

    @Override
    public BaseApplication getApplication() {
        return BaseApplication.getInstance();
    }

    protected void runOnWorkerThread(Runnable runnable) {
        THREAD_POOL_EXECUTOR.execute(runnable);
    }
}
