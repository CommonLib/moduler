package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseModel<VM> {

    private VM mViewModel;

    public void setViewModel(VM viewModel) {
        mViewModel = viewModel;
    }

    public VM getViewModel() {
        return mViewModel;
    }

    protected void onModelCreate() {
    }

    protected void onModelDestroy() {
    }
}
