package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.databinding.BaseObservable;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.base.ShareDataViewModel;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 5/24/17.
 */

public abstract class BaseViewModel<M extends BaseModel> extends BaseObservable
        implements ViewForegroundSwitchListener, LifecycleObserver {
    private M mModel;

    public BaseViewModel() {
        super();
        try {
            mModel = GenericUtil.instanceT(this, 0);
        } catch (Exception e) {
            LogUtil.e("instance " + this.getClass().getName() + " model error");
        }
        onCreate();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onViewCreate() {
        LogUtil.d(this.getClass().getName() + " onViewCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onViewStart() {
        LogUtil.d(this.getClass().getName() + " onViewStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onViewResume() {
        LogUtil.d(this.getClass().getName() + " onViewResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onViewPause() {
        LogUtil.d(this.getClass().getName() + " onViewPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onViewStop() {
        LogUtil.d(this.getClass().getName() + " onViewStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onViewDestroy() {
        LogUtil.d(this.getClass().getName() + " onViewDestroy");
        onDestroy();
    }

    public M getModel() {
        return mModel;
    }

    protected void shareData(String key, Object data) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders.getInstance()
                .get(ShareDataViewModel.class);
        shareDataViewModel.put(key, data);
    }

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders.getInstance()
                .get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }

    protected void onCreate() {
    }

    protected void onDestroy() {
        ViewModelProviders.getInstance().remove(this.getClass());
        getModel().onDestroy();
    }

    @Override
    public void onViewForeground() {
        synchronized (LiveData.class) {
            if (LiveData.hasBlock) {
                LogUtil.d("detect there is block tasks waiting for push view");
                LiveData.hasBlock = false;
                LiveData.class.notifyAll();
            }
        }
    }

    @Override
    public void onViewBackground() {

    }

    public LiveData<View> asyncInflate(@LayoutRes int layoutId, LayoutInflater layoutInflater,
                                       ViewGroup viewGroup) {
        return getModel().asyncInflate(layoutId, layoutInflater, viewGroup);
    }

    public LiveData<Void> onPullToRefresh() {
        return getModel().onPullToRefresh();
    }
}
