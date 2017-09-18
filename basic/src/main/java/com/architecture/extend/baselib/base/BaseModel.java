package com.architecture.extend.baselib.base;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseModel<VMC> implements ModelLayer {

    private VMC mViewModel;

    @Override
    public void setViewModel(ViewModelLayer viewModel) {
        mViewModel = (VMC) viewModel;
    }

    public VMC getViewModel() {
        return mViewModel;
    }



    protected void onModelCreate(){}
    protected void onModelDestroy(){}
}
