package com.architecture.extend.baselib.exception;

/**
 * Created by byang059 on 2018/12/6.
 */

public class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }
}
