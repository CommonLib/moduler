package com.architecture.extend.baselib.mvvm;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by byang059 on 11/16/17.
 */

public class AsyncTransforms {

    private static HandlerThread mViewModelThread = new HandlerThread("viewModel",
            android.os.Process.THREAD_PRIORITY_FOREGROUND);
    private static Handler viewModelHandler;

    static {
        mViewModelThread.start();
        viewModelHandler = new Handler(mViewModelThread.getLooper());
    }

    private AsyncTransforms() {
    }

    public static <T, Y> LiveData<Y> map(LiveData<T> liveData, final Function<T, Y> func) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(liveData, onChanged -> {
            //UI
            viewModelHandler.post(() -> {
                Y apply = func.apply(onChanged);
                result.postValue(apply);
            });
        });
        return result;
    }
}
