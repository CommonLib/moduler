package com.architecture.extend.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.mvvm.BaseModel;
import com.architecture.extend.baselib.mvvm.NetworkCacheResource;
import com.architecture.extend.baselib.storage.remote.RetrofitHelper;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by byang059 on 5/27/17.
 */

@Singleton
public class MainModel extends BaseModel {

    WeatherDao weatherDao;

    @Override
    public void init() {
        WeatherDatabase db = Room
                .databaseBuilder(BaseApplication.getInstance(), WeatherDatabase.class, "weather")
                .build();
        weatherDao = db.weatherDao();
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

    public NetworkCacheResource<Weather> getPullToRefreshResource() {
        return new NetworkCacheResource<Weather>() {

            @NonNull
            @Override
            protected Call<Weather> getCall() {
                LogUtil.d("getCall");
                MainApiService service = RetrofitHelper.getInstance()
                        .getService(MainApiService.class);
                return service.getWeather("北京");
            }

            @Override
            protected void onFetchFailed(Response<Weather> response, Throwable throwable) {
                LogUtil.d("onFetchFailed => " + response);
            }
        };
    }
}
