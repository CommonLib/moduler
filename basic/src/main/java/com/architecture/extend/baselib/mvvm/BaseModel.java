package com.architecture.extend.baselib.mvvm;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseModel {

    public BaseModel() {
        onCreate();
    }

    public LiveData<Void> onPullToRefresh() {
        return null;
    }

    public void onCreate() {

    }

    public void onDestroy() {

    }

    public LiveData<View> asyncInflate(@LayoutRes final int layoutId,
                                       final LayoutInflater layoutInflater,
                                       final ViewGroup viewGroup) {
        return new LiveData<>(new AsyncProducer<View>() {
            @Override
            public void produce(LiveData<View> liveData) {
                View view = layoutInflater.inflate(layoutId, viewGroup, false);
                liveData.postValue(view);
            }
        });
    }
}
