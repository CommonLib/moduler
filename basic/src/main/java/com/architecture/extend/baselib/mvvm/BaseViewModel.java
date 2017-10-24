package com.architecture.extend.baselib.mvvm;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.databinding.BaseObservable;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.base.SharedViewModel;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by byang059 on 5/24/17.
 */

public abstract class BaseViewModel<M extends BaseModel> extends BaseObservable
        implements ViewForegroundSwitchListener, LifecycleObserver {
    private M mModel;
    private ArrayList<LiveData> mLives;

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

    protected void shareData(String key, Object data) {
        SharedViewModel sharedViewModel = ViewModelProviders.getInstance()
                .get(SharedViewModel.class);
        sharedViewModel.put(key, data);
    }

    protected Object getSharedData(String key) {
        SharedViewModel sharedViewModel = ViewModelProviders.getInstance()
                .get(SharedViewModel.class);
        return sharedViewModel.get(key);
    }

    protected void onCreate() {
    }

    protected void onDestroy() {
        ViewModelProviders.getInstance().remove(this.getClass());
        disposeLiveData();
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

    public void putLiveData(LiveData data) {
        if (mLives == null) {
            mLives = new ArrayList<>();
        }
        mLives.add(data);
    }

    private void disposeLiveData() {
        if (mLives != null) {
            LogUtil.d("destroyed all live data, sum = " + mLives.size());
            for (LiveData liveData : mLives) {
                liveData.dispose();
            }
        }
    }

    public LiveData<View> asyncInflate(@LayoutRes int layoutId, LayoutInflater layoutInflater,
                                       ViewGroup viewGroup) {
        return getModel().asyncInflate(layoutId, layoutInflater, viewGroup);
    }

    public LiveData<Void> onPullToRefresh() {
        return getModel().onPullToRefresh();
    }
}
