package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.BaseApplication;

import dagger.Component;

/**
 * Created by byang059 on 1/31/18.
 */

@Component(modules = {ObjectInjectionModule.class})
public interface BaseApplicationComponent {
    void inject(BaseApplication application);
}
