package com.architecture.extend.baselib.mvvm;

import android.os.Handler;
import android.support.annotation.MainThread;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.util.LogUtil;

import java.lang.ref.WeakReference;
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

    private Flowable<LiveResponse<T>> mModelObservable;
    private ProduceAble<T> mProducer;
    private WeakReference<ViewAble> mView;
    private LiveCallBack<T> mViewCallBack;
    private LiveViewModelCallBack<T> mViewModelCallBack;
    private Handler mHandler = BaseApplication.getInstance().getHandler();
    private FlowableEmitter<LiveResponse<T>> mEmitter;
    private static ExecutorService mViewModelThreadService = Executors.newFixedThreadPool(1);
    private boolean mBackPressure;
    private Disposable mSubscribe;
    private static final int RESULT_VIEW_DESTROY = 0;
    private static final int RESULT_VIEW_BACKGROUND = 1;
    private static final int RESULT_SUCCESS = 2;

    public void setProducer(Producer<T> producer){
        setProducer(producer, true);
    }

    public void setProducer(AsyncProducer<T> producer){
        setProducer(producer, true);
    }

    public void setProducer(Producer<T> producer, boolean backPressure){
        mBackPressure = backPressure;
        mModelObservable = initFlowAble(producer, backPressure);
    }

    public void setProducer(AsyncProducer<T> producer, boolean backPressure){
        mBackPressure = backPressure;
        Flowable<LiveResponse<T>> flowAble = initFlowAble(producer, backPressure);
        mModelObservable = flowAble.subscribeOn(Schedulers.io());
    }

    private Flowable<LiveResponse<T>> initFlowAble(ProduceAble<T> producer, boolean backPressure) {
        mProducer = producer;
        FlowableOnSubscribe<LiveResponse<T>> source = new FlowableOnSubscribe<LiveResponse<T>>() {
            @Override
            public void subscribe(FlowableEmitter<LiveResponse<T>> emitter) throws Exception {
                mEmitter = emitter;
                mProducer.produce(LiveData.this);
            }
        };
        if (backPressure) {
            return Flowable.create(source, BackpressureStrategy.BUFFER);
        } else {
            return Flowable.create(source, BackpressureStrategy.LATEST);
        }
    }

    /**
     * @param viewAble
     * @param callBack
     */
    @MainThread
    public void subscribe(ViewAble viewAble, LiveCallBack<T> callBack) {
        mView = new WeakReference<>(viewAble);
        mViewCallBack = callBack;

        if(mViewModelCallBack != null){
            mViewModelCallBack.onStart();
            //TODO impl mulit subscribe and return last data when new subscribe
        }
        callBack.onStart();
        viewAble.getViewModel().putLiveData(this);
        mSubscribe = mModelObservable.observeOn(Schedulers.from(mViewModelThreadService))
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
                    public void accept(LiveResponse<T> liveResponse) throws Exception {
                        while (shouldContinueNotifyView(liveResponse)) {
                            synchronized (LiveData.class) {
                                try {
                                    hasBlock = true;
                                    LiveData.class.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    private boolean shouldContinueNotifyView(LiveResponse<T> liveResponse) {
        boolean shouldContinueNotifyView = false;
        int result = notifyViewChange(liveResponse);
        switch (result) {
            case RESULT_VIEW_BACKGROUND:
                shouldContinueNotifyView = true;
                break;
            case RESULT_VIEW_DESTROY:
                shouldContinueNotifyView = false;
                dispose();
                break;
            case RESULT_SUCCESS:
                shouldContinueNotifyView = false;
                break;
        }
        return shouldContinueNotifyView && mBackPressure;
    }

    private int notifyViewChange(final LiveResponse<T> response) {
        if (mView == null) {
            return RESULT_VIEW_DESTROY;
        }

        ViewAble view = mView.get();
        if (view == null || view.isDestroyed()) {
            return RESULT_VIEW_DESTROY;
        }

        if (!view.isForeground()) {
            return RESULT_VIEW_BACKGROUND;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mViewCallBack != null) {
                    mViewCallBack.onResponse(response);
                }
            }
        });
        return RESULT_SUCCESS;
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
}
