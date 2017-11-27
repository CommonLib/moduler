package com.architecture.extend.baselib.mvvm;

import retrofit2.Response;

/**
 * Created by byang059 on 11/17/17.
 */

public class ResponseWrapper<RequestType> {

    public Response<ApiResponse<RequestType>> response;
    public Throwable throwable;
    public int errorCode;

    public ResponseWrapper(Response<ApiResponse<RequestType>> response) {
        this.response = response;
    }

    public ResponseWrapper(int errorCode, Throwable throwable) {
        this.errorCode = errorCode;
        this.throwable = throwable;
    }

    public boolean isSuccessful() {
        return response != null && response.isSuccessful();
    }
}
