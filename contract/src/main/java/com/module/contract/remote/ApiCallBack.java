package com.module.contract.remote;

import android.support.annotation.NonNull;

import com.architecture.extend.baselib.storage.remote.ApiException;
import com.architecture.extend.baselib.storage.remote.RetrofitCallBack;

import retrofit2.HttpException;

/**
 * Created by byang059 on 12/13/17.
 */

public abstract class ApiCallBack<T> extends RetrofitCallBack<T> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    @Override
    protected boolean isBusinessCodeSuccess(int businessCode) {
        return businessCode == 200;
    }

    @Override
    protected void handleHttpError(HttpException error, int httpCode) {
        switch (httpCode) {
            case UNAUTHORIZED:
                break;
            case FORBIDDEN:
                break;
            case NOT_FOUND:
                break;
            case REQUEST_TIMEOUT:
                break;
            case INTERNAL_SERVER_ERROR:
                break;
            case BAD_GATEWAY:
                break;
            case SERVICE_UNAVAILABLE:
                break;
            case GATEWAY_TIMEOUT:
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleApiError(int errorBusinessCode, @NonNull ApiException t) {
        super.handleApiError(errorBusinessCode, t);
    }
}
