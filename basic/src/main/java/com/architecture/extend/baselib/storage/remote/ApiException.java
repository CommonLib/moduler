package com.architecture.extend.baselib.storage.remote;

/**
 * Created by byang059 on 11/23/17.
 */

public class ApiException extends Exception {

    public int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public String toString() {
        return "ApiException{" + "code=" + code + " msg=" + getMessage() + '}';
    }
}
