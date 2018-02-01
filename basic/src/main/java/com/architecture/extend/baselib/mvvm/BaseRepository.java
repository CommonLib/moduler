package com.architecture.extend.baselib.mvvm;

import android.os.AsyncTask;

import java.util.concurrent.Executor;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository {

    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public BaseRepository() {
        onCreate();
    }

    public void onCreate() {
    }

    protected void runOnWorkerThread(Runnable runnable) {
        THREAD_POOL_EXECUTOR.execute(runnable);
    }
}
