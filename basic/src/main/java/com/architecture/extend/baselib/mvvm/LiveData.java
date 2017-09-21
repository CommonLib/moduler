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

    private final Flowable<T> mModelObservable;
    private ProduceAble<T> mProducer;
    private WeakReference<ViewAble> mView;
    private UiCallBack<T> mViewCallBack;
    private ViewModelCallBack<T> mViewModelCallBack;
    private Handler mHandler = BaseApplication.getInstance().getHandler();
    private FlowableEmitter<T> mEmitter;
    private static ExecutorService mViewModelThreadService = Executors.newFixedThreadPool(1);
    private boolean mBackPressure;
    private Disposable mSubscribe;

    public LiveData(Producer<T> producer) {
        this(producer, true);
    }

    public LiveData(Producer<T> producer, boolean backPressure) {
        mBackPressure = backPressure;
        mModelObservable = initFlowAble(producer, backPressure);
    }

    public LiveData(AsyncProducer<T> producer) {
        this(producer, true);
    }

    public LiveData(AsyncProducer<T> producer, boolean backPressure) {
        mBackPressure = backPressure;
        Flowable<T> flowAble = initFlowAble(producer, backPressure);
        mModelObservable = flowAble.subscribeOn(Schedulers.io());
    }

    private Flowable<T> initFlowAble(ProduceAble<T> producer, boolean backPressure) {
        mProducer = producer;
        FlowableOnSubscribe<T> source = new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
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

    private boolean notifyViewChange(final T t) {
        if (mView == null) {
            return false;
        }
        ViewAble view = mView.get();
        if (view == null || !view.isForeground()) {
            return false;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mViewCallBack != null) {
                    mViewCallBack.onDataReady(t);
                }
            }
        });
        return true;
    }

    /**
     * @param viewAble
     * @param callBack
     */
    @MainThread
    public void observe(ViewAble viewAble, UiCallBack<T> callBack) {
        mView = new WeakReference<>(viewAble);
        mViewCallBack = callBack;

        mSubscribe = mModelObservable.observeOn(Schedulers.from(mViewModelThreadService))
                .map(new Function<T, T>() {
                    @Override
                    public T apply(T t) throws Exception {
                        T result = null;
                        if (mViewModelCallBack != null) {
                            result = mViewModelCallBack.onDealWithData(t);
                        } else {
                            result = t;
                        }
                        return result;
                    }
                }).observeOn(Schedulers.io()).subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        while (!notifyViewChange(t) && mBackPressure) {
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

    @MainThread
    public void cancelSubcription() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
    }

    public void intercept(ViewModelCallBack<T> callBack) {
        mViewModelCallBack = callBack;
    }

    public void postValue(T t) {
        if (mEmitter != null) {
            mEmitter.onNext(t);
        } else {
            LogUtil.e("not found emitter in liveData, post value failed: value = " + t.toString());
        }
    }
}
