package com.architecture.extend.architecture;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.SystemClock;

import com.architecture.extend.baselib.mvvm.AsyncTransforms;
import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.architecture.extend.baselib.mvvm.Resource;
import com.architecture.extend.baselib.util.LogUtil;
import com.module.contract.pic.IPicService;
import com.module.contract.remote.ApiBundleResource;

import javax.inject.Inject;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel {

    @Inject
    IPicService mIPicService;

    @Inject
    MainRepository mMainRepository;


    private MutableLiveData<String> mStringMutableLiveData;
    private LiveData<Resource<Weather>> mPullToRefresh;
    private ApiBundleResource<Weather, Weather> mPullToRefreshResource;

    public LiveData getUserString(String abc,String bcd) {
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
            mPullToRefreshResource = mMainRepository.getPullToRefreshResource();
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
