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
    private ArrayList<LiveData> mLiveDatas;
    private LiveData<View> mInflateLiveData;

    public BaseModel() {
        onCreate();
    }

    public LiveData<View> asyncInflate(final LayoutInflater layoutInflater,
                                       final ViewGroup viewGroup, @LayoutRes final int layoutId) {
        if(mInflateLiveData == null){
            mInflateLiveData = new LiveData<>(new AsyncProducer<View>() {
                @Override
                public void produce(LiveData<View> liveData) {
                    View inflate = layoutInflater.inflate(layoutId, viewGroup, false);
                    liveData.postValue(inflate);
                }
            });
        }
        return mInflateLiveData;
    }

    public LiveData<Void> onPullToRefresh() {
        return null;
    }

    public void onCreate() {

    }

    public void onDestroy() {
        disposeLiveData();
    }

    public void putLiveData(LiveData data){
        if(mLiveDatas == null){
            mLiveDatas = new ArrayList<>();
        }
        mLiveDatas.add(data);
    }

    private void disposeLiveData(){
        if(mLiveDatas != null){
            LogUtil.d("destroyed all live data, sum = " + mLiveDatas.size());
            for (LiveData liveData : mLiveDatas) {
                liveData.dispose();
            }
        }
    }
}
