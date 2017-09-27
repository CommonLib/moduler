package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 9/20/17.
 */

public abstract class LiveCallBack<T> {
    public abstract void onComplete(T t);

    public void onStart() {
    }

    public void onCacheReturn(T t) {

    }

    public void onProgressUpdate(int progress) {
    }

    public void onError(Throwable t) {
    }

    /**
     * @param response
     */
    public final void onResponse(LiveResponse<T> response) {
        switch (response.type) {
            case LiveResponse.TYPE_CACHE:
                onCacheReturn(response.result);
                break;
            case LiveResponse.TYPE_PROGRESS:
                onProgressUpdate(response.progress);
                break;
            case LiveResponse.TYPE_RESULT:
                onComplete(response.result);
                break;
            case LiveResponse.TYPE_ERROR:
                onError(response.error);
                break;
            default:
        }
    }
}
