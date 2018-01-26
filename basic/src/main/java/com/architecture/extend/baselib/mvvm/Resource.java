package com.architecture.extend.baselib.mvvm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by byang059 on 11/16/17.
 */

public class Resource<T> {

    public static final int STATE_SUCCESS = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_CACHE = 3;
    public static final int STATE_PROGRESS = 4;

    @NonNull
    public final int status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;
    @Nullable
    public final int progress;

    private Resource(@NonNull int status, @Nullable T data, @Nullable String message,
                     @Nullable int progress) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.progress = progress;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        //TODO add cache item
        return new Resource<>(STATE_SUCCESS, data, null, 0);
    }

    public static <T> Resource<T> error(String msg) {
        return new Resource<>(STATE_ERROR, null, msg, 0);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(STATE_LOADING, null, null, 0);
    }

    public static <T> Resource<T> cache(@Nullable T data) {
        return new Resource<>(STATE_CACHE, data, null, 0);
    }

    public static <T> Resource<T> progress(@Nullable T data, int progress) {
        return new Resource<>(STATE_PROGRESS, data, null, progress);
    }

    public boolean isSuccess(){
        return status == STATE_SUCCESS;
    }

    public boolean isError(){
        return status == STATE_ERROR;
    }

    public boolean isLoading(){
        return status == STATE_LOADING;
    }

    public boolean isCache(){
        return status == STATE_CACHE;
    }

    public boolean isProgress(){
        return status == STATE_PROGRESS;
    }
}
