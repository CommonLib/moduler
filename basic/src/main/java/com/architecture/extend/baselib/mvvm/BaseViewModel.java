package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.baselib.BaseApplication;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * Created by byang059 on 5/24/17.
 */

public abstract class BaseViewModel extends ViewModel
        implements ViewForegroundSwitchListener, LifecycleObserver {

    @Inject
    Executor mExecutor;

    public BaseApplication getApplication() {
        return BaseApplication.getInstance();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onViewCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onViewStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onViewResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onViewPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onViewStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onViewDestroy() {
    }

    @Override
    public void onViewForeground() {

    }

    @Override
    public void onViewBackground() {

    }

    public LiveData<View> asyncInflate(@LayoutRes final int layoutId,
                                       final LayoutInflater layoutInflater,
                                       final ViewGroup viewGroup) {
        final MutableLiveData<View> liveData = new MutableLiveData<>();
        runOnWorkerThread(() -> {
            View view = layoutInflater.inflate(layoutId, viewGroup, false);
            liveData.postValue(view);
        });
        return liveData;
    }

    public BaseApplication getApplicationContext() {
        return BaseApplication.getInstance();
    }

    public void startPage(Bundle bundle, String path) {
        Postcard build = ARouter.getInstance().build(path);
        if (bundle != null) {
            build.with(bundle);
        }
        build.navigation();
    }

    public void startPageForResult(Bundle bundle, String path, Activity activity, int requestCode) {
        Postcard build = ARouter.getInstance().build(path);
        if (bundle != null) {
            build.with(bundle);
        }
        build.navigation(activity, requestCode);
    }

    protected void runOnWorkerThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    public void onPullToRefresh() {

    }
}
