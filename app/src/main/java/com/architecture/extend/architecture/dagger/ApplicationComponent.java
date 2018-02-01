package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.MainApplication;
import com.architecture.extend.baselib.dagger.BaseApplicationComponent;

import dagger.Component;

/**
 * Created by byang059 on 1/31/18.
 */

@Component(dependencies = BaseApplicationComponent.class, modules = MainModule.class)
public interface ApplicationComponent {
    void inject(MainApplication application);
}
