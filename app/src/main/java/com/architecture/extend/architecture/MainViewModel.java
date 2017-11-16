package com.architecture.extend.architecture;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.SystemClock;

import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.architecture.extend.baselib.mvvm.AsyncTransforms;
import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel<MainModel> {

    private MutableLiveData<String> mStringMutableLiveData;

    public android.arch.lifecycle.LiveData getUserString() {
        if (mStringMutableLiveData != null) {
            return mStringMutableLiveData;
        }
        mStringMutableLiveData = new MutableLiveData<>();
        LiveData liveData = AsyncTransforms.map(mStringMutableLiveData, new Function<String, String>() {
            @Override
            public String apply(String input) {
                LogUtil.d("Thread.currentThread() " + Thread.currentThread().getName());
                SystemClock.sleep(5000);
                return input + "viewModel";
            }
        });
        mStringMutableLiveData.setValue("first value");
        mStringMutableLiveData.postValue("second value");
        return liveData;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }
}
