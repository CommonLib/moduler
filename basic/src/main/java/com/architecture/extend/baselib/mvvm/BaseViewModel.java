package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 5/24/17.
 */

public abstract class BaseViewModel<M extends BaseModel> extends ViewModel
        implements ViewForegroundSwitchListener, LifecycleObserver {
    private M mModel;

    public BaseViewModel() {
        super();
        onCreate();
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
        onDestroy();
    }

    public M getModel() {
        return mModel;
    }

    @CallSuper
    protected void onCreate() {
        try {
            mModel = GenericUtil.instanceT(this, 0);
            mModel.init();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("instance " + this.getClass().getName() + " model error");
        }
        ARouter.getInstance().inject(this);
    }

    @CallSuper
    protected void onDestroy() {
    }

    @Override
    public void onViewForeground() {

    }

    @Override
    public void onViewBackground() {

    }

    public LiveData<View> asyncInflate(@LayoutRes int layoutId, LayoutInflater layoutInflater,
                                       ViewGroup viewGroup) {
        MutableLiveData<View> liveData = new MutableLiveData<>();
        getModel().asyncInflate(liveData, layoutId, layoutInflater, viewGroup);
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
}
