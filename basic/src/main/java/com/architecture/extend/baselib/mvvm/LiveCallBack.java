package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.architecture.extend.baselib.BaseApplication;

import java.lang.ref.WeakReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by byang059 on 9/20/17.
 */

public abstract class LiveCallBack<T>
        implements FlowableOnSubscribe<LiveResponse<T>>, Consumer<LiveResponse<T>>,
                   LifecycleObserver {
    private FlowableEmitter<LiveResponse<T>> mEmitter;
    private WeakReference<ViewAble> mViewable;
    private Disposable mDisposable;
    private static final int RESULT_VIEW_DESTROY = 0;
    private static final int RESULT_VIEW_BACKGROUND = 1;
    private static final int RESULT_SUCCESS = 2;
    private Handler mHandler = BaseApplication.getInstance().getHandler();
    private LiveData<T> mLiveData;
    private boolean hasBlock;

    public abstract void onComplete(T t);

    public void onStart() {
    }

    public void onCacheReturn(T t) {

    }

    public void onProgressUpdate(int progress) {
    }

    public void onError(Throwable t) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onViewResume() {
        if (hasBlock) {
            synchronized (this) {
                hasBlock = false;
                notify();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onViewDestroy() {
        dispose();
        if (mViewable != null && mViewable.get() != null) {
            mViewable.get().getLifecycle().removeObserver(this);
        }
        if (mLiveData != null) {
            mLiveData.getViewCallBacks().remove(this);
        }
    }

    /**
     * @param response
     */
    private void dispatchResponse(LiveResponse<T> response) {
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

    @Override
    public void subscribe(FlowableEmitter<LiveResponse<T>> emitter) throws Exception {
        mEmitter = emitter;
    }

    public void onResponse(LiveResponse<T> response) {
        if (mEmitter != null && isSubscribe()) {
            mEmitter.onNext(response);
        }
    }

    @MainThread
    private boolean isSubscribe() {
        return mDisposable != null && !mDisposable.isDisposed();
    }

    @Override
    @WorkerThread
    public void accept(LiveResponse<T> tLiveResponse) throws Exception {
        while (shouldContinueNotifyView(tLiveResponse)) {
            synchronized (this) {
                try {
                    hasBlock = true;
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
        return shouldContinueNotifyView;
    }

    @MainThread
    private void dispose() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mEmitter = null;
        }
    }

    private int notifyViewChange(final LiveResponse<T> response) {
        if (mViewable == null) {
            return RESULT_VIEW_DESTROY;
        }

        ViewAble view = mViewable.get();
        if (view == null || view.isDestroyed()) {
            return RESULT_VIEW_DESTROY;
        }

        if (!view.isForeground()) {
            return RESULT_VIEW_BACKGROUND;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                dispatchResponse(response);
            }
        });
        return RESULT_SUCCESS;
    }

    public void initResponseSubscribe(ViewAble view, BackpressureStrategy mode,
                                      LiveData<T> liveData) {
        view.getLifecycle().addObserver(this);
        mViewable = new WeakReference<>(view);
        mLiveData = liveData;
        Flowable<LiveResponse<T>> flowAble = Flowable.create(this, mode);
        mDisposable = flowAble.observeOn(Schedulers.io()).subscribe(this);
    }
}
