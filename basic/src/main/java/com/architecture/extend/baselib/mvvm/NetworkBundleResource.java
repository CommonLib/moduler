package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
        mCacheSource = loadFromCache();
        mResult = new MediatorLiveData<>();
    }

    public void start() {
        mResult.removeSource(mCacheSource);
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
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        // we re-attach mCacheSource as a new source,
        // it will dispatch its latest value quickly
        mResult.addSource(mCacheSource, newData -> mResult.setValue(Resource.cache(newData)));
        mResult.addSource(apiResponse, response -> {
            mResult.removeSource(apiResponse);
            mResult.removeSource(mCacheSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                saveResultAndReInit(response.response.body());
            } else {
                onFetchFailed(response.response, response.throwable);
                mResult.addSource(mCacheSource,
                        newData -> mResult.setValue(Resource.error(response.errorMessage)));
            }
        });
    }

    protected LiveData<ApiResponse<RequestType>> createCall() {
        MutableLiveData<ApiResponse<RequestType>> liveData = new MutableLiveData<>();
        Call<RequestType> call = getCall();
        call.enqueue(new Callback<RequestType>() {
            @Override
            public void onResponse(@NonNull Call<RequestType> call,
                                   @NonNull Response<RequestType> response) {
                BaseApplication.getInstance().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        liveData.setValue(new ApiResponse<>(response));
                    }
                }, 5000);

            }

            @Override
            public void onFailure(@NonNull Call<RequestType> call, @NonNull Throwable t) {
                liveData.setValue(new ApiResponse<>(t));
            }
        });
        return liveData;
    }

    protected abstract @NonNull
    Call<RequestType> getCall();

    protected abstract void onFetchFailed(Response<RequestType> response, Throwable throwable);

    @MainThread
    private void saveResultAndReInit(RequestType body) {
        Flowable.fromCallable(() -> body).subscribeOn(Schedulers.io())
                .doOnNext(this::saveCallResult).observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestType -> {
                    mCacheSource = loadFromCache();
                    mResult.addSource(mCacheSource,
                            newData -> mResult.setValue(Resource.success(newData)));
                });
    }

    protected abstract void saveCallResult(RequestType body);

    public final LiveData<Resource<ResultType>> getLiveData() {
        return mResult;
    }

    public final LiveData<Resource<ResultType>> getStartedLiveData() {
        start();
        return mResult;
    }
}
