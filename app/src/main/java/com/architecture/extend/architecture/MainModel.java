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
        if(mData == null){
            mData = new LiveData<>(new AsyncProducer<String>() {
                @Override
                public void produce(LiveData<String> liveData) {
                    SystemClock.sleep(3000);
                    liveData.postValue("first value");
                    liveData.postValue("second value");
                }
            });
        }
        return mData;
    }
}
