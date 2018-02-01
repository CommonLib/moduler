package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;

import dagger.Component;

/**
 * Created by byang059 on 1/31/18.
 */

@Component(modules = BaseModule.class)
public interface BaseApplicationComponent {
    void inject(BaseApplication application);
    //必须显示暴露此依赖，负责依赖者无法找到此依赖
    ConfigureInfo provideConfigureInfo();
}
