package com.architecture.extend.baselib.mvvm;

import android.support.annotation.MainThread;

import com.architecture.extend.baselib.util.LogUtil;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by byang059 on 9/20/17.
 */

public class LiveData<T> {
    public static boolean hasBlock = false;

    private ProduceAble<T> mProducer;
    private ArrayList<LiveCallBack<T>> mViewCallBacks;
    private LiveViewModelCallBack<T> mViewModelCallBack;
    private FlowableEmitter<LiveResponse<T>> mEmitter;
    private static ExecutorService mViewModelThreadService = Executors.newFixedThreadPool(1);
    private Disposable mSubscribe;

    public void setProducer(Producer<T> producer) {
        mProducer = producer;
    }

    public void setProducer(AsyncProducer<T> producer) {
        mProducer = producer;
    }

    private Flowable<LiveResponse<T>> initFlowAble(final ProduceAble<T> producer,
                                                   BackpressureStrategy mode) {
        FlowableOnSubscribe<LiveResponse<T>> source = new FlowableOnSubscribe<LiveResponse<T>>() {
            @Override
            public void subscribe(FlowableEmitter<LiveResponse<T>> emitter) throws Exception {
                mEmitter = emitter;
                producer.produce(LiveData.this);
            }
        };
        return Flowable.create(source, mode);
    }

    /**
     * @param view
     * @param callBack
     */
    @MainThread
    public void subscribe(ViewAble view, LiveCallBack<T> callBack) {
        subscribe(view, callBack, BackpressureStrategy.LATEST);
    }

    /**
     * @param view
     * @param callBack
     */
    @MainThread
    public void subscribe(ViewAble view, LiveCallBack<T> callBack, BackpressureStrategy mode) {
        if (mViewCallBacks == null) {
            mViewCallBacks = new ArrayList<>();
        }

        Flowable<LiveResponse<T>> flowAble = Flowable.create(callBack, mode);
        Disposable subscribe = flowAble.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe
                (callBack);
        callBack.setViewable(view);
        callBack.setDisposable(subscribe);
        callBack.setLiveData(this);
        mViewCallBacks.add(callBack);

        if (isSubscribe()) {
            return;
        }

        Flowable<LiveResponse<T>> modelObservable = initFlowAble(mProducer, mode);
        if (mProducer instanceof AsyncProducer) {
            modelObservable = modelObservable.subscribeOn(Schedulers.io());
        }

        if (mViewModelCallBack != null) {
            mViewModelCallBack.onStart();
        }
        callBack.onStart();
        view.getViewModel().putLiveData(this);
        mSubscribe = modelObservable.observeOn(Schedulers.from(mViewModelThreadService))
                .map(new Function<LiveResponse<T>, LiveResponse<T>>() {
                    @Override
                    public LiveResponse<T> apply(LiveResponse<T> liveResponse) throws Exception {
                        if (mViewModelCallBack != null) {
                            liveResponse = mViewModelCallBack.onResponse(liveResponse);
                        }
                        return liveResponse;
                    }
                }).observeOn(Schedulers.io()).subscribe(new Consumer<LiveResponse<T>>() {
                    @Override
                    public void accept(LiveResponse<T> tLiveResponse) throws Exception {
                        for (LiveCallBack<T> callBack : mViewCallBacks) {
                            callBack.onNext(tLiveResponse);
                        }
                    }
                });
    }

    @MainThread
    public void dispose() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
    }

    @MainThread
    public boolean isSubscribe() {
        return mSubscribe != null && !mSubscribe.isDisposed();
    }

    public void intercept(LiveViewModelCallBack<T> callBack) {
        mViewModelCallBack = callBack;
    }

    public void postValue(T t) {
        postValue(new LiveResponse<>(t));
    }

    public void postCache(T t) {
        postValue(new LiveResponse<>(t, LiveResponse.TYPE_CACHE));
    }

    public void postProgress(int progress) {
        postValue(new LiveResponse<T>(progress));
    }

    public void postError(Exception e) {
        postValue(new LiveResponse<T>(e));
    }

    private void postValue(LiveResponse<T> response) {
        if (mEmitter != null) {
            mEmitter.onNext(response);
        } else {
            LogUtil.e("not found emitter in liveData, post value failed: value = " + response
                    .toString());
        }
    }

    public ArrayList<LiveCallBack<T>> getViewCallBacks() {
        return mViewCallBacks;
    }
}
