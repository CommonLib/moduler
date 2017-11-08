package com.reference.apublic.web;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.module.contract.web.IWebService;

/**
 * Created by byang059 on 9/18/17.
 */

public class WebViewModel extends BaseViewModel<WebModel> {

    @Autowired
    IWebService mIWebService;

    @Override
    protected void onCreate() {
        super.onCreate();
        String abc = mIWebService.dealString("abc");
    }
}
