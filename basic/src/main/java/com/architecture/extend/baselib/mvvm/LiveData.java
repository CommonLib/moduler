package com.architecture.extend.baselib.mvvm;

import android.os.Handler;
import android.support.annotation.MainThread;

import com.architecture.extend.baselib.BaseApplication;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
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
    private Subscription mSubscription;
    private static ExecutorService mViewModelThreadService = Executors.newFixedThreadPool(1);
    private boolean mBackPressure;

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
                T result = mProducer.produce(LiveData.this);
                mEmitter.onNext(result);
            }
        };
        if (backPressure) {
            return Flowable.create(source, BackpressureStrategy.BUFFER);
        } else {
            return Flowable.create(source, BackpressureStrategy.BUFFER);
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

        mModelObservable.observeOn(Schedulers.from(mViewModelThreadService))
                .map(new Function<T, T>() {
                    @Override
                    public T apply(T t) throws Exception {
                        T result = null;
                        if (mViewModelCallBack != null) {
                            result = mViewModelCallBack.onInterceptData(t);
                        } else {
                            result = t;
                        }
                        return result;
                    }
                }).observeOn(Schedulers.io()).subscribe(new Subscriber<T>() {


            @Override
            public void onSubscribe(Subscription subscription) {
                mSubscription = subscription;
                subscription.request(1);
                if (mViewModelCallBack != null) {
                    mViewModelCallBack.onSubscribe(subscription);
                }
            }

            @Override
            public void onNext(T t) {
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
                if (mSubscription != null) {
                    mSubscription.request(1);
                }
            }

            @Override
            public void onError(Throwable t) {
                if (mViewModelCallBack != null) {
                    mViewModelCallBack.onError(t);
                }
            }

            @Override
            public void onComplete() {
                if (mViewModelCallBack != null) {
                    mViewModelCallBack.onComplete();
                }
            }
        });
    }

    public void intercept(ViewModelCallBack<T> callBack) {
        mViewModelCallBack = callBack;
    }

    public void postValue(T t) {
        if (mEmitter != null) {
            mEmitter.onNext(t);
        }
    }
}
