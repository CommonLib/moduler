package com.architecture.extend.architecture;

import android.os.SystemClock;

import com.architecture.extend.baselib.mvvm.AsyncProducer;
import com.architecture.extend.baselib.mvvm.BaseModel;
import com.architecture.extend.baselib.mvvm.LiveData;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainModel extends BaseModel {

    private LiveData<String> mData;

    public LiveData<String> readDatabase(final String a, final String b) {
        if (mData == null) {
            mData = new LiveData<>();
            mData.setProducer(new AsyncProducer<String>() {
                @Override
                public void produce(LiveData<String> liveData) {
                    SystemClock.sleep(1000);
                    liveData.postCache("cache data");
                    SystemClock.sleep(1000);
                    liveData.postValue("first value");
                    liveData.postProgress(50);
                    SystemClock.sleep(1000);
                    liveData.postValue("second value");
                    liveData.postProgress(100);
                    SystemClock.sleep(1000);
                    liveData.postValue("result value");
                    liveData.postError(new Exception());
                }
            });
        }
        return mData;
    }

    @Override
    public LiveData<Void> onPullToRefresh() {
        LiveData<Void> data = new LiveData<>();
        data.setProducer(new AsyncProducer<Void>() {
            @Override
            public void produce(LiveData<Void> liveData) {
                SystemClock.sleep(3000);
                liveData.postValue(null);
            }
        });
        return data;

    }
}
