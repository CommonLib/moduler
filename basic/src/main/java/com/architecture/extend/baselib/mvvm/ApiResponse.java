package com.architecture.extend.baselib.mvvm;

import retrofit2.Response;

/**
 * Created by byang059 on 11/17/17.
 */

public class ApiResponse<RequestType> {

    public String errorMessage;
    public Response<RequestType> response;
    public Throwable throwable;

    public ApiResponse(Response<RequestType> response) {
        this.response = response;
    }

    public ApiResponse(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isSuccessful() {
        return response != null && response.isSuccessful();
    }
}
