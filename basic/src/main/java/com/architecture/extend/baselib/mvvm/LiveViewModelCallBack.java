package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 9/27/17.
 */

public abstract class LiveViewModelCallBack<T> {
    public abstract T onComplete(T value);

    public void onStart() {
    }

    public T onCacheReturn(T cache) {
        return cache;
    }

    public int onProgressUpdate(int progress) {
        return progress;
    }

    public Throwable onError(Throwable error) {
        return error;
    }

    /**
     * @param response
     */
    public final LiveResponse<T> onResponse(LiveResponse<T> response) {
        switch (response.type) {
            case LiveResponse.TYPE_CACHE:
                response.result = onCacheReturn(response.result);
                break;
            case LiveResponse.TYPE_PROGRESS:
                response.progress = onProgressUpdate(response.progress);
                break;
            case LiveResponse.TYPE_RESULT:
                response.result = onComplete(response.result);
                break;
            case LiveResponse.TYPE_ERROR:
                response.error = onError(response.error);
                break;
            default:
        }
        return response;
    }
}
