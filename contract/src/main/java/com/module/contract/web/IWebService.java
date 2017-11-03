package com.module.contract.web;

import android.content.Context;

import com.architecture.extend.baselib.router.Contract;

/**
 * Created by byang059 on 9/15/17.
 */

public interface IWebService extends Contract{
    void openWeb(Context context);
    String dealString(String a);
}
