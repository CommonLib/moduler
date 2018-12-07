package com.module.contract.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.base.Bean;
import com.architecture.extend.baselib.storage.local.ACache;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.LogUtil;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by byang059 on 11/22/17.
 */

public abstract class ApiCacheResource<ResultType extends Bean>
        extends ApiBundleResource<ResultType, ResultType> {

    @NonNull
    @Override
    protected LiveData<ResultType> loadFromCache() {
        final MutableLiveData<ResultType> cacheLiveData = new MutableLiveData<>();
        Flowable.fromCallable(() -> cacheLiveData).observeOn(Schedulers.io())
                .subscribe(liveData -> {
                    Class<Object> resultType = GenericUtil
                            .getGenericsSuperType(ApiCacheResource.this, 0);
                    ACache aCache = ACache.get(BaseApplication.getInstance());
                    Object asObject = aCache.getAsObject(resultType.getSimpleName());
                    LogUtil.d("ApiCacheResource loadFromCache() get cache is =>" + asObject);
                    if (asObject == null) {
                        liveData.postValue(null);
                    } else {
                        liveData.postValue((ResultType) asObject);
                    }
                });
        return cacheLiveData;
    }

    @Override
    protected void saveCallResult(final ResultType result) {
        LogUtil.d("NetworkCacheResource_saveCallResult() result = " + result);
        Flowable.fromCallable(() -> result).observeOn(Schedulers.io()).subscribe(value -> {
            LogUtil.d("NetworkCacheResource_accept() save value to cache");
            Class<Object> resultType = GenericUtil.getGenericsSuperType(ApiCacheResource.this, 0);
            ACache aCache = ACache.get(BaseApplication.getInstance());
            aCache.put(resultType.getSimpleName(), value);
        });
    }
}
