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

public abstract class RetrofitCallBack<T> implements Callback<ApiResponse<T>> {

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
            handleHttpError(error, code);
            onFailure(code, error);
        } else if (t instanceof ApiException) {
            ApiException apiException = (ApiException) t;
            handleApiError(apiException.code, apiException);
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

    protected void handleHttpError(HttpException error, int httpCode) {
    }

    protected void handleApiError(int errorBusinessCode, @NonNull ApiException t) {
    }

    protected abstract boolean isBusinessCodeSuccess(int businessCode);

    protected abstract void onSuccess(T t, Response<ApiResponse<T>> response);

    protected abstract void onFailure(int errorCode, Throwable t);
}
