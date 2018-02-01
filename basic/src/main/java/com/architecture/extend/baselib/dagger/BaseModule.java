package com.architecture.extend.baselib.dagger;

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
}
