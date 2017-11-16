package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.Executor;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseModel {

    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public BaseModel() {
    }

    public void onPullToRefresh(MutableLiveData<Void> liveData) {
    }

    public MutableLiveData<View> asyncInflate(final MutableLiveData<View> liveData,
                                              @LayoutRes final int layoutId,
                                              final LayoutInflater layoutInflater,
                                              final ViewGroup viewGroup) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                View view = layoutInflater.inflate(layoutId, viewGroup, false);
                liveData.postValue(view);
            }
        });
        return liveData;
    }

    protected void runOnWorkerThread(Runnable runnable) {
        THREAD_POOL_EXECUTOR.execute(runnable);
    }
}
