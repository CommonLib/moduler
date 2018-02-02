package com.architecture.extend.baselib.dagger;

import android.os.Handler;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by byang059 on 2/2/18.
 */

@Singleton
@Component(modules = BaseApplicationModule.class)
public interface BaseApplicationComponent {
    ConfigureInfo provideConfigureInfo();

    Handler provideHandler();

    BaseApplication provideApplication();
}
