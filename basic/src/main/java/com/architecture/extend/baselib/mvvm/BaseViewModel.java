package com.architecture.extend.baselib.mvvm;

import android.databinding.BaseObservable;

import com.architecture.extend.baselib.base.ShareDataViewModel;
import com.architecture.extend.baselib.util.GenericUtil;

/**
 * Created by byang059 on 5/24/17.
 */

public abstract class BaseViewModel<M extends BaseModel> extends BaseObservable {
    private M mModel;

    public BaseViewModel() {
        super();
        mModel = GenericUtil.instanceT(this, 0);
        onCreate();
    }

    public void onViewCreate() {
    }

    public void onViewStart() {
    }

    public void onViewRestart() {
    }

    public void onViewResume() {
    }

    public void onViewPause() {
    }

    public void onViewStop() {
    }

    public void onViewDestroy() {
        onDestroy();
        mModel.onModelDestroy();
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
    }
}
