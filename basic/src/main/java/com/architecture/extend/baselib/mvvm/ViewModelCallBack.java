package com.architecture.extend.baselib.mvvm;

import org.reactivestreams.Subscription;

/**
 * Created by byang059 on 9/20/17.
 */

public abstract class ViewModelCallBack<T> {
    public abstract T onInterceptData(T t);

    public void onError(Throwable t) {

    }

    public void onComplete() {

    }

    public void onSubscribe(Subscription s) {

    }
}
