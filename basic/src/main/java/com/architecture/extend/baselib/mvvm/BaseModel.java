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
    }


    public LiveData<View> asyncInflate(final LayoutInflater layoutInflater,
                                       final ViewGroup viewGroup, @LayoutRes final int layoutId) {
        return new LiveData<>(new AsyncProducer<View>() {
            @Override
            public void produce(LiveData<View> liveData) {
                View inflate = layoutInflater.inflate(layoutId, viewGroup, false);
                liveData.postValue(inflate);
            }
        });
    }

    public LiveData<Void> onPullToRefresh() {
        return new LiveData<>(new AsyncProducer<Void>() {
            @Override
            public void produce(LiveData<Void> liveData) {

            }
        });
    }
}
