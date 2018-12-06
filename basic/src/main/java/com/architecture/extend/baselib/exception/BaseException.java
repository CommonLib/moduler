package com.architecture.extend.baselib.exception;

/**
 * Created by byang059 on 2018/12/6.
 */

public class BaseException extends Exception {
    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException() {
        super();
    }
}
