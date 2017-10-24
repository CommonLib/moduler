package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.support.annotation.MainThread;

import com.architecture.extend.baselib.BaseApplication;

import java.lang.ref.WeakReference;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by byang059 on 9/20/17.
 */

public abstract class LiveCallBack<T>
        implements FlowableOnSubscribe<LiveResponse<T>>, Consumer<LiveResponse<T>>,
                   LifecycleObserver {
    private FlowableEmitter<LiveResponse<T>> mEmitter;
    private WeakReference<ViewAble> mViewable;
    private Disposable disposable;
    private static final int RESULT_VIEW_DESTROY = 0;
    private static final int RESULT_VIEW_BACKGROUND = 1;
    private static final int RESULT_SUCCESS = 2;
    private Handler mHandler = BaseApplication.getInstance().getHandler();
    private LiveData<T> mLiveData;

    public abstract void onComplete(T t);

    public void onStart() {
    }

    public void onCacheReturn(T t) {

    }

    public void onProgressUpdate(int progress) {
    }

    public void onError(Throwable t) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onViewDestroy() {
        if (mViewable != null && mViewable.get() != null) {
            dispose();
            mViewable.get().getLifecycle().removeObserver(this);
            if (mLiveData != null) {
                mLiveData.getViewCallBacks().remove(this);
            }
        }
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

    public void setLiveData(LiveData<T> liveData) {
        mLiveData = liveData;
    }

    @Override
    public void subscribe(FlowableEmitter<LiveResponse<T>> emitter) throws Exception {
        mEmitter = emitter;
    }

    public void onNext(LiveResponse<T> response) {
        if (mEmitter != null && isSubscribe()) {
            mEmitter.onNext(response);
        }
    }

    @MainThread
    public boolean isSubscribe() {
        return disposable != null && !disposable.isDisposed();
    }

    public void setViewable(ViewAble viewable) {
        mViewable = new WeakReference<>(viewable);
        viewable.getLifecycle().addObserver(this);
    }

    public void setDisposable(Disposable disposable) {
        this.disposable = disposable;
    }

    private void notifySpecificView(LiveResponse<T> tLiveResponse) {
        while (shouldContinueNotifyView(tLiveResponse)) {
            synchronized (LiveData.class) {
                try {
                    LiveData.hasBlock = true;
                    LiveData.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void accept(LiveResponse<T> tLiveResponse) throws Exception {
        notifySpecificView(tLiveResponse);
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
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
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
                onResponse(response);
            }
        });
        return RESULT_SUCCESS;
    }
}
