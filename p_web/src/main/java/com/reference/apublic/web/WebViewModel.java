package com.reference.apublic.web;

import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.module.contract.web.IWebService;

import javax.inject.Inject;

/**
 * Created by byang059 on 9/18/17.
 */

public class WebViewModel extends BaseViewModel {

    @Inject
    IWebService mIWebService;

    @Override
    public void onViewCreate() {
        super.onViewCreate();
        String abc = mIWebService.dealString("abc");
    }
}
