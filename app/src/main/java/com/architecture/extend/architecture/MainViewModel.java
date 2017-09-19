package com.architecture.extend.architecture;

import com.architecture.extend.baselib.mvvm.BaseViewModel;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel<MainModel> {

    public Flowable<String> getUserString() {
        Flowable<String> observable = Flowable.fromPublisher(new Flowable<String>() {
            @Override
            protected void subscribeActual(Subscriber<? super String> s) {
                String result = getModel().readDatabase("a", "b");
                s.onNext(result);
            }
        });
        return observable;
    }

    @Override
    public void onViewCreate() {
        super.onViewCreate();
        shareData("abc", "sharted abc");
    }
}
