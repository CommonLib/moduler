package com.architecture.extend.baselib.base;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseModel<VMC> {

    private BaseViewModel mViewModel;

    public void setViewModel(BaseViewModel viewModel) {
        mViewModel = viewModel;
    }

    public VMC getViewModel() {
        return (VMC) mViewModel;
    }

    protected void onModelCreate() {
    }

    protected void onModelDestroy() {
    }
}
