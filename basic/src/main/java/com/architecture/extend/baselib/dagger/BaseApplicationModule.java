package com.architecture.extend.baselib.dagger;

import android.os.Handler;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ObjectInjectionModule;

/**
 * Created by byang059 on 2/1/18.
 */

@Module(includes = ObjectInjectionModule.class)
public abstract class BaseApplicationModule {

    @Singleton
    @Provides
    static ConfigureInfo provideConfigureInfo(){
        return ConfigureInfo.defaultConfigure();
    }

    @Singleton
    @Provides
    static Handler provideHandler(){
        return new Handler();
    }

    @Singleton
    @Provides
    static BaseApplication provideApplication(){
        return BaseApplication.getInstance();
    }
}
