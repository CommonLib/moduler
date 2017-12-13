package com.module.contract.remote;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.architecture.extend.baselib.mvvm.ApiResponse;
import com.architecture.extend.baselib.mvvm.NetworkBundleResource;
import com.architecture.extend.baselib.mvvm.ResponseWrapper;
import com.architecture.extend.baselib.util.LogUtil;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by byang059 on 12/13/17.
 */

public abstract class ApiBundleResource<ResultType, RequestType>
        extends NetworkBundleResource<ResultType, RequestType> {

    @NonNull
    @Override
    protected Callback<ApiResponse<RequestType>> getCallBack(
            final MutableLiveData<ResponseWrapper<RequestType>> liveData) {
        return new ApiCallBack<RequestType>() {
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
    }
}
