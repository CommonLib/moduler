package com.architecture.extend.baselib.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;

import com.architecture.extend.baselib.util.GenericUtil;

/**
 * Created by byang059 on 5/24/17.
 */

public abstract class BaseViewModel<VC, M extends BaseModel, MC>
        extends ViewModel implements ViewModelLayer, LifecycleObserver, LifecycleOwner {
    private ViewLayer mView;
    private M mModel;

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    public BaseViewModel() {
        super();
        mModel = GenericUtil.instanceT(this, 1);
        if (mModel == null) {
            throw new IllegalArgumentException("can't instance model");
        }
        mModel.setViewModel(this);
        onViewModelCreate();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
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

    public VC getView() {
        return (VC) mView;
    }

    public MC getModel() {
        return (MC) mModel;
    }

    @Override
    public void setView(ViewLayer view) {
        mView = view;
    }

    @Override
    protected final void onCleared() {
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        onViewModelDestroy();
        mModel.onModelDestroy();
    }

    protected void shareData(String key, Object data){
        BaseActivity scope = mView.getBaseActivity();
        ShareDataViewModel shareDataViewModel = ViewModelProviders.of(scope)
                .get(ShareDataViewModel.class);
        shareDataViewModel.put(key, data);
    }

    protected <T> T getSharedData(String key){
        BaseActivity scope = mView.getBaseActivity();
        ShareDataViewModel shareDataViewModel = ViewModelProviders.of(scope)
                .get(ShareDataViewModel.class);
        return (T) shareDataViewModel.get(key);
    }

    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    protected void onViewModelCreate(){}
    protected void onViewModelDestroy(){}
}
