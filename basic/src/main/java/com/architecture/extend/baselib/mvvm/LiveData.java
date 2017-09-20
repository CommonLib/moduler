package com.architecture.extend.baselib.mvvm;

import android.os.Handler;
import android.support.annotation.MainThread;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.util.LogUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by byang059 on 9/20/17.
 */

public class LiveData<T> {

    private final Flowable<T> mModelObservable;
    private WeakReference<ViewAble> mView;
    private UiCallBack<T> mViewCallBack;
    private ViewModelCallBack<T> mViewModelCallBack;
    private Handler mHandler = BaseApplication.getInstance().getHandler();
    private FlowableEmitter<T> mEmitter;

    public LiveData() {
        mModelObservable = Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                mEmitter = emitter;
            }
        }, BackpressureStrategy.BUFFER);
    }

    private boolean notifyViewChange(final T t) {
        ViewAble view = mView.get();
        if (view != null && view.isForeground()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mViewCallBack.onDataReady(t);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param viewAble
     * @param callBack
     */
    @MainThread
    public void observe(ViewAble viewAble, UiCallBack<T> callBack) {
        mView = new WeakReference<>(viewAble);
        mViewCallBack = callBack;

        mModelObservable.observeOn(Schedulers.io()).subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
                if (mViewModelCallBack != null) {
                    mViewModelCallBack.onSubscribe(s);
                }
            }

            @Override
            public void onNext(T t) {
                //this can invoke in sub Thread.
                // t is just get from model layer, viewModel need to pre deal with
                T result = null;
                if (mViewModelCallBack != null) {
                    result = mViewModelCallBack.onInterceptData(t);
                } else {
                    result = t;
                }

                while (!notifyViewChange(result)) {
                    //store call back and when act back to resume,then push data to callback to
                    // update ui
                    synchronized (LiveData.class) {
                        try {
                            LogUtil.d("wait()");
                            LiveData.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
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

    public void setValue(T t) {
        if (mEmitter != null) {
            mEmitter.onNext(t);
        }
    }
}
