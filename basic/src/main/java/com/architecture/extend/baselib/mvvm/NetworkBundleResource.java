package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.util.LogUtil;

import java.lang.reflect.Field;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by byang059 on 11/16/17.
 */

public abstract class NetworkBundleResource<ResultType, RequestType> {
    private final MediatorLiveData<Resource<ResultType>> mResult;
    private LiveData<ResultType> mCacheSource;

    @MainThread
    public NetworkBundleResource() {
        //refactor
        mResult = new MediatorLiveData<>();
    }

    public void start() {
        if (mCacheSource != null) {
            mResult.removeSource(mCacheSource);
        }
        mCacheSource = loadFromCache();
        mResult.setValue(Resource.loading());
        mResult.addSource(mCacheSource, data -> {
            mResult.removeSource(mCacheSource);
            if (shouldFetch(data)) {
                fetchFromNetwork();
            } else {
                mResult.addSource(mCacheSource,
                        newData -> mResult.setValue(Resource.success(newData)));
            }
        });
    }

    protected boolean shouldFetch(ResultType data) {
        return true;
    }

    protected abstract @NonNull
    LiveData<ResultType> loadFromCache();

    private void fetchFromNetwork() {
        final LiveData<ResponseWrapper<RequestType>> apiResponse = createCall();
        // we re-attach mCacheSource as a new source,
        // it will dispatch its latest value quickly
        mResult.addSource(mCacheSource, newData -> {
            if (newData != null) {
                mResult.setValue(Resource.cache(newData));
            }
        });
        mResult.addSource(apiResponse, response -> {
            mResult.removeSource(apiResponse);
            mResult.removeSource(mCacheSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                //also need business fault
                ApiResponse<RequestType> body = response.response.body();
                if (body != null) {
                    saveResultAndReInit(body.data);
                }
            } else {
                onFetchFailed(response.response, response.throwable);
                mResult.addSource(mCacheSource, resultType -> mResult
                        .setValue(Resource.error(response.throwable.getMessage())));
            }
        });
    }

    protected LiveData<ResponseWrapper<RequestType>> createCall() {
        final MutableLiveData<ResponseWrapper<RequestType>> liveData = new MutableLiveData<>();
        Call<ApiResponse<RequestType>> call = getCall();
        Callback<ApiResponse<RequestType>> retrofitCallBack = getCallBack(liveData);
        putCallBackInRequest(call.request(), retrofitCallBack);
        call.enqueue(retrofitCallBack);
        return liveData;
    }

    @NonNull
    protected abstract Callback<ApiResponse<RequestType>> getCallBack(
            final MutableLiveData<ResponseWrapper<RequestType>> liveData);

    private void putCallBackInRequest(Request request,
                                      Callback<ApiResponse<RequestType>> retrofitCallBack) {
        Class<? extends Request> clazz = request.getClass();
        try {
            Field tagField = clazz.getDeclaredField("tag");
            tagField.setAccessible(true);
            tagField.set(request, retrofitCallBack);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract @NonNull
    Call<ApiResponse<RequestType>> getCall();

    protected abstract void onFetchFailed(Response<ApiResponse<RequestType>> response,
                                          Throwable throwable);

    @MainThread
    private void saveResultAndReInit(final RequestType result) {
        LogUtil.d("NetworkBundleResource_saveResultAndReInit() result = " + result);
        if (result == null) {
            mResult.setValue(Resource.success(null));
            return;
        }
        Flowable.fromCallable(() -> result).subscribeOn(Schedulers.io())
                .doOnNext(this::saveCallResult).observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestType -> {
                    mCacheSource = loadFromCache();
                    mResult.addSource(mCacheSource, newData -> {
                        LogUtil.d("NetworkBundleResource_onChanged() notify ui from cache");
                        mResult.setValue(Resource.success(newData));
                    });
                });
    }

    protected abstract void saveCallResult(RequestType result);

    public final LiveData<Resource<ResultType>> getLiveData() {
        return mResult;
    }

    public final LiveData<Resource<ResultType>> getStartedLiveData() {
        start();
        return mResult;
    }
}
