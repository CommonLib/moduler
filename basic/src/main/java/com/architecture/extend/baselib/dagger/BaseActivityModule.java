package com.architecture.extend.baselib.dagger;

import android.app.Activity;

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
    static RxPermissions provodeRxPermissions(Activity activity) {
        return new RxPermissions(activity);
    }
}
