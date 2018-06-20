package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * Created by byang059 on 2018/6/13.
 */

@Module
public class BaseActivityModule {

    @ActicityScope
    @Provides
    static RxPermissions provideRxPermissions(BaseActivity activity) {
        return new RxPermissions(activity);
    }
}
