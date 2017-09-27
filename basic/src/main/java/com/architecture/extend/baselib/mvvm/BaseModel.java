package com.architecture.extend.baselib.mvvm;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseModel {
    private ArrayList<LiveData> mLives;

    public BaseModel() {
        onCreate();
    }

    public LiveData<Void> onPullToRefresh() {
        return null;
    }

    public void onCreate() {

    }

    public void onDestroy() {
        disposeLiveData();
    }

    public void putLiveData(LiveData data) {
        if (mLives == null) {
            mLives = new ArrayList<>();
        }
        mLives.add(data);
    }

    private void disposeLiveData() {
        if (mLives != null) {
            LogUtil.d("destroyed all live data, sum = " + mLives.size());
            for (LiveData liveData : mLives) {
                liveData.dispose();
            }
        }
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
