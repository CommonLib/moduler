package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.base.Bean;
import com.architecture.extend.baselib.storage.local.ACache;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.LogUtil;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by byang059 on 11/22/17.
 */

public abstract class NetworkCacheResource<ResultType extends Bean>
        extends NetworkBundleResource<ResultType, ResultType> {

    @NonNull
    @Override
    protected LiveData<ResultType> loadFromCache() {
        final MutableLiveData<ResultType> cacheLiveData = new MutableLiveData<>();
        Flowable.fromCallable(new Callable<MutableLiveData<ResultType>>() {
            @Override
            public MutableLiveData<ResultType> call() throws Exception {
                return cacheLiveData;
            }
        }).observeOn(Schedulers.io()).subscribe(new Consumer<MutableLiveData<ResultType>>() {
            @Override
            public void accept(MutableLiveData<ResultType> liveData) throws Exception {
                Class<Object> resultType = GenericUtil
                        .getGenericsSuperType(NetworkCacheResource.this, 0);
                ACache aCache = ACache.get(BaseApplication.getInstance());
                Object asObject = aCache.getAsObject(resultType.getSimpleName());
                LogUtil.d("NetworkCacheResource loadFromCache() get cache is =>" + asObject);
                if (asObject == null) {
                    liveData.postValue(null);
                } else {
                    liveData.postValue((ResultType) asObject);
                }
            }
        });
        return cacheLiveData;
    }

    @Override
    protected void saveCallResult(final ResultType result) {
        LogUtil.d("NetworkCacheResource_saveCallResult() result = " + result);
        Flowable.fromCallable(new Callable<ResultType>() {
            @Override
            public ResultType call() throws Exception {
                return result;
            }
        }).observeOn(Schedulers.io()).subscribe(new Consumer<ResultType>() {
            @Override
            public void accept(ResultType value) throws Exception {
                LogUtil.d("NetworkCacheResource_accept() save value to cache");
                Class<Object> resultType = GenericUtil
                        .getGenericsSuperType(NetworkCacheResource.this, 0);
                ACache aCache = ACache.get(BaseApplication.getInstance());
                aCache.put(resultType.getSimpleName(), value);
            }
        });
    }
}
