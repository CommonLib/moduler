package com.architecture.extend.architecture;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.SystemClock;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.architecture.extend.baselib.mvvm.AsyncTransforms;
import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.architecture.extend.baselib.mvvm.NetworkBundleResource;
import com.architecture.extend.baselib.mvvm.Resource;
import com.architecture.extend.baselib.util.LogUtil;
import com.module.contract.pic.IPicService;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel<MainModel> {

    @Autowired
    IPicService mIPicService;

    private MutableLiveData<String> mStringMutableLiveData;
    private LiveData<Resource<Weather>> mPullToRefresh;
    private NetworkBundleResource<Weather, Weather> mPullToRefreshResource;

    public LiveData getUserString() {
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
        mIPicService.playPic(getApplicationContext());
        return liveData;
    }

    public LiveData<Resource<Weather>> getPullToRefresh() {
        if(mPullToRefresh == null){
            mPullToRefreshResource = getModel().getPullToRefreshResource();
            mPullToRefresh = mPullToRefreshResource.getLiveData();
        }
        return mPullToRefresh;
    }

    public void onPullToRefresh() {
        if(mPullToRefreshResource != null){
            mPullToRefreshResource.start();
        }
    }
}
