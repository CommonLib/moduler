package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.base.Bean;
import com.architecture.extend.baselib.storage.local.ACache;
import com.architecture.extend.baselib.util.GenericUtil;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by byang059 on 11/22/17.
 */

public abstract class NetworkCacheResource<ResultType extends Bean>
        extends NetworkBundleResource<ResultType, ResultType> {

    @NonNull
    @Override
    protected LiveData<ResultType> loadFromCache() {
        MutableLiveData<ResultType> cacheLiveData = new MutableLiveData<>();
        Flowable.fromCallable(() -> cacheLiveData).observeOn(Schedulers.io())
                .subscribe(liveData -> {
                    Class<Object> resultType = GenericUtil
                            .getGenericsSuperType(NetworkCacheResource.this, 0);
                    ACache aCache = ACache.get(BaseApplication.getInstance());
                    Object asObject = aCache.getAsObject(resultType.getSimpleName());
                    if (asObject == null) {
                        liveData.postValue(null);
                    } else {
                        liveData.postValue((ResultType) asObject);
                    }
                });
        return cacheLiveData;
    }

    @Override
    protected void saveCallResult(ResultType body) {
        Flowable.fromCallable(() -> body).observeOn(Schedulers.io()).subscribe(value -> {
            Class<Object> resultType = GenericUtil
                    .getGenericsSuperType(NetworkCacheResource.this, 0);
            ACache aCache = ACache.get(BaseApplication.getInstance());
            aCache.put(resultType.getSimpleName(), value);
        });
    }
}
