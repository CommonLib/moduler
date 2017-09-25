package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 9/25/17.
 */

public class LiveResponse<T> {
    public static final int TYPE_PROGRESS = 0;
    public static final int TYPE_CACHE = 1;
    public static final int TYPE_RESULT = 2;
    public static final int TYPE_ERROR = 3;


    public LiveResponse(T result) {
        this.result = result;
        this.type = TYPE_RESULT;
    }

    public LiveResponse(int progress) {
        this.progress = progress;
        this.type = TYPE_PROGRESS;
    }

    public LiveResponse(Exception error) {
        this.error = error;
        this.type = TYPE_ERROR;
    }

    public LiveResponse(T result, int type) {
        this.result = result;
        this.type = type;
    }

    public T result;
    public int progress;
    public Exception error;
    public int type;

    @Override
    public String toString() {
        return "LiveResponse{" + "result=" + result + ", progress=" + progress + ", type=" + type
                + '}';
    }
}
