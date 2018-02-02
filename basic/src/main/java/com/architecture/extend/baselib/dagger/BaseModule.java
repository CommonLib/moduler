package com.architecture.extend.baselib.dagger;

import android.os.Handler;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by byang059 on 2/1/18.
 */

@Module(includes = ObjectInjectionModule.class)
public abstract class BaseModule {

    @Provides
    static ConfigureInfo provideConfigureInfo(){
        return ConfigureInfo.defaultConfigure();
    }

    @Provides
    static Handler provideHandler(){
        return new Handler();
    }

    @Provides
    static BaseApplication provideApplication(){
        return BaseApplication.getInstance();
    }
}
