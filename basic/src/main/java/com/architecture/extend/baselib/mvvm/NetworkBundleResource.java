package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.architecture.extend.baselib.storage.remote.ApiCallBack;
import com.architecture.extend.baselib.util.LogUtil;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.Call;
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
        if(mCacheSource != null){
            mResult.removeSource(mCacheSource);
        }
        mCacheSource = loadFromCache();
        mResult.setValue((Resource<ResultType>) Resource.loading());
        mResult.addSource(mCacheSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType data) {
                mResult.removeSource(mCacheSource);
                if (shouldFetch(data)) {
                    fetchFromNetwork();
                } else {
                    mResult.addSource(mCacheSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType newData) {
                            mResult.setValue(Resource.success(newData));
                        }
                    });
                }
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
        mResult.addSource(mCacheSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType newData) {
                if (newData != null) {
                    mResult.setValue(Resource.cache(newData));
                }
            }
        });
        mResult.addSource(apiResponse, new Observer<ResponseWrapper<RequestType>>() {
            @Override
            public void onChanged(@Nullable final ResponseWrapper<RequestType> response) {
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
                    mResult.addSource(mCacheSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType resultType) {
                            mResult.setValue((Resource<ResultType>) Resource
                                    .error(response.throwable.getMessage()));
                        }
                    });
                }
            }
        });
    }

    protected LiveData<ResponseWrapper<RequestType>> createCall() {
        final MutableLiveData<ResponseWrapper<RequestType>> liveData = new MutableLiveData<>();
        Call<ApiResponse<RequestType>> call = getCall();
        ApiCallBack<RequestType> apiCallBack = new ApiCallBack<RequestType>() {
            @Override
            protected void onSuccess(RequestType requestType,
                                     Response<ApiResponse<RequestType>> response) {
                liveData.setValue(new ResponseWrapper<>(response));
            }

            @Override
            protected void onFailure(int errorCode, Throwable t) {
                liveData.setValue(
                        (ResponseWrapper<RequestType>) new ResponseWrapper<>(errorCode, t));
            }

            @Override
            public void onDownLoadProgressUpdate(boolean done, long current, long totalSize,
                                                 long speed) {
                LogUtil.d("onDownLoadProgressUpdate done = " + done + " current" + current + " "
                        + "totalSize" + totalSize + " speed" + speed);
                super.onDownLoadProgressUpdate(done, current, totalSize, speed);
            }

            @Override
            public void onUploadProgressUpdate(boolean done, long current, long totalSize,
                                               long speed) {
                LogUtil.d("onUploadProgressUpdate done = " + done + " current" + current + " "
                        + "totalSize" + totalSize + " speed" + speed);
                super.onUploadProgressUpdate(done, current, totalSize, speed);
            }
        };
        putCallBackInRequest(call.request(), apiCallBack);
        call.enqueue(apiCallBack);
        return liveData;
    }

    private void putCallBackInRequest(Request request, ApiCallBack<RequestType> apiCallBack) {
        Class<? extends Request> clazz = request.getClass();
        try {
            Field tagField = clazz.getDeclaredField("tag");
            tagField.setAccessible(true);
            tagField.set(request, apiCallBack);
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
            mResult.setValue((Resource<ResultType>) Resource.success(null));
            return;
        }
        Flowable.fromCallable(new Callable<RequestType>() {
            @Override
            public RequestType call() throws Exception {
                return result;
            }
        }).subscribeOn(Schedulers.io()).doOnNext(new Consumer<RequestType>() {
            @Override
            public void accept(RequestType requestType) throws Exception {
                saveCallResult(requestType);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RequestType>() {
            @Override
            public void accept(RequestType requestType) throws Exception {
                mCacheSource = loadFromCache();
                mResult.addSource(mCacheSource, new Observer<ResultType>() {
                    @Override
                    public void onChanged(@Nullable ResultType newData) {
                        LogUtil.d("NetworkBundleResource_onChanged() notify ui from cache");
                        mResult.setValue(Resource.success(newData));
                    }
                });
            }
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
