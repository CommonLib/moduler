package com.architecture.extend.baselib.storage.remote;

import android.support.annotation.NonNull;

import com.architecture.extend.baselib.mvvm.ApiResponse;
import com.architecture.extend.baselib.util.LogUtil;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by byang059 on 11/23/17.
 */

public abstract class ApiCallBack<T> implements Callback<ApiResponse<T>> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int PARSE_ERROR = 1001;
    private static final int UN_KNOWN = 1002;

    @Override
    public void onResponse(@NonNull Call<ApiResponse<T>> call,
                           @NonNull Response<ApiResponse<T>> response) {
        if (response.isSuccessful()) {
            ApiResponse<T> body = response.body();
            if (body != null) {
                if (isBusinessCodeSuccess(body.status)) {
                    onSuccess(body.data, response);
                } else {
                    onFailure(call, new ApiException(body.message, body.status));
                }
            } else {
                onSuccess(null, response);
            }
        } else {
            onFailure(call, new HttpException(response));
        }
    }

    @Override
    public void onFailure(@NonNull Call<ApiResponse<T>> call, @NonNull Throwable t) {
        Throwable throwable = t;
        while (throwable.getCause() != null) {
            t = throwable;
            throwable = throwable.getCause();
        }

        LogUtil.exception(t);

        if (t instanceof HttpException) {
            HttpException error = (HttpException) t;
            int code = error.code();
            dealWithHttpError(error, code);
            onFailure(code, error);
        } else if (t instanceof ApiException) {
            ApiException apiException = (ApiException) t;
            dealWithApiError(apiException.code, apiException);
            onFailure(apiException.code, apiException);
        } else if (t instanceof JsonParseException || t instanceof JSONException
                || t instanceof ParseException) {
            //parse error
            onFailure(PARSE_ERROR, t);
        } else {
            //other error
            onFailure(UN_KNOWN, t);
        }
    }

    public void onDownLoadProgressUpdate(boolean done, long current, long totalSize, long speed) {

    }

    public void onUploadProgressUpdate(boolean done, long current, long totalSize, long speed) {

    }

    protected void dealWithHttpError(HttpException error, int httpCode) {
    }

    protected void dealWithApiError(int errorBusinessCode, @NonNull ApiException t) {
    }

    protected boolean isBusinessCodeSuccess(int businessCode) {
        return businessCode == 200;
    }

    protected abstract void onSuccess(T t, Response<ApiResponse<T>> response);

    protected abstract void onFailure(int errorCode, Throwable t);
}
