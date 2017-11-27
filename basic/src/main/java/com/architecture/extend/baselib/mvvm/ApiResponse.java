package com.architecture.extend.baselib.mvvm;

import com.architecture.extend.baselib.base.Bean;

/**
 * Created by byang059 on 11/22/17.
 */

public class ApiResponse<T> extends Bean {
    public int status;
    public String message;
    public T data;

    @Override
    public String toString() {
        return "ApiResponse{" + "status=" + status + ", message='" + message + '\'' + ", data="
                + data + '}';
    }
}
