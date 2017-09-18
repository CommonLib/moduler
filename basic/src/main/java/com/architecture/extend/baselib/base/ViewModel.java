package com.architecture.extend.baselib.base;

import android.databinding.BaseObservable;

import com.architecture.extend.baselib.util.ReflectUtil;

/**
 * Created by byang059 on 5/24/17.
 */

public abstract class ViewModel<VC, MC> extends BaseObservable implements ViewModelLayer {
    private ViewLayer mView;
    private BaseModel mModel;

    public ViewModel() {
        super();
        Configuration configuration = getClass().getAnnotation(Configuration.class);
        if(configuration != null){
            mModel = (BaseModel) ReflectUtil.newInstance(configuration.model());
            mModel.setViewModel(this);
            mModel.onModelCreate();
        }
        onViewModelCreate();
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
        onViewModelDestroy();
        mModel.onModelDestroy();
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

    protected void shareData(String key, Object data) {
        ShareDataViewModel shareDataViewModel = (ShareDataViewModel) ViewModelProviders
                .getInstance().get(ShareDataViewModel.class);
        shareDataViewModel.put(key, data);
    }

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = (ShareDataViewModel) ViewModelProviders
                .getInstance().get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }

    protected void onViewModelCreate() {
    }

    protected void onViewModelDestroy() {
    }
}
