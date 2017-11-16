package com.architecture.extend.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.os.SystemClock;
import android.support.annotation.MainThread;

import com.architecture.extend.baselib.mvvm.BaseModel;

import javax.inject.Singleton;

/**
 * Created by byang059 on 5/27/17.
 */

@Singleton
public class MainModel extends BaseModel {


    @MainThread
    public void readDatabase(final String a, final String b, MutableLiveData<String> data) {
        /*data.setProducer(new AsyncProducer<String>() {
            @Override
            public void produce(MutableLiveData<String> liveData) {
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
        });*/

        data.setValue("first value");
        data.postValue("first value");
    }

    @Override
    public void onPullToRefresh(final MutableLiveData<Void> liveData) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                liveData.postValue(null);
            }
        });
    }
}
