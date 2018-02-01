package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.MainApplication;
import com.architecture.extend.baselib.dagger.ViewModelInjectionModule;

import dagger.Component;

/**
 * Created by byang059 on 1/31/18.
 */

@Component(modules = {MainModule.class, ViewModelInjectionModule.class})
public interface ApplicationComponent {
    void inject(MainApplication application);
}
