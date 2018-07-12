package com.architecture.extend.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.mvvm.ApiResponse;
import com.architecture.extend.baselib.mvvm.BaseRepository;
import com.architecture.extend.baselib.util.LogUtil;
import com.module.contract.remote.ApiCacheResource;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainRepository extends BaseRepository {

    @Inject
    MainApiService mMainApiService;

    WeatherDao weatherDao;

    @Inject
    SecondRepository mSecondRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        WeatherDatabase db = Room
                .databaseBuilder(BaseApplication.getInstance(), WeatherDatabase.class, "weather")
                .build();
        weatherDao = db.weatherDao();
        LogUtil.i("mSecondRepository" + mSecondRepository);
    }

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

    public ApiCacheResource<Weather> getPullToRefreshResource() {
        return new ApiCacheResource<Weather>() {

            @NonNull
            @Override
            protected Call<ApiResponse<Weather>> getCall() {
                LogUtil.d("getCall");
                return mMainApiService.getWeather("北京");
            }

            @Override
            protected void onFetchFailed(Response<ApiResponse<Weather>> response, Throwable
                    throwable) {
                LogUtil.d("onFetchFailed => " + response + " errorMsg = " + throwable);
            }
        };
    }
}
