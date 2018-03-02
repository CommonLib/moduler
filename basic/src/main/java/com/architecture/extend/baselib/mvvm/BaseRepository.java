package com.architecture.extend.baselib.mvvm;

import android.os.AsyncTask;

import com.architecture.extend.baselib.BaseApplication;

import java.util.concurrent.Executor;

import dagger.android.InjectAble;
import dagger.android.ObjectInjection;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository implements InjectAble {

    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public BaseRepository() {
        ObjectInjection.inject(this, BaseApplication.getInstance());
        onCreate();
    }

    public void onCreate() {

    }

    protected void runOnWorkerThread(Runnable runnable) {
        THREAD_POOL_EXECUTOR.execute(runnable);
    }
}
