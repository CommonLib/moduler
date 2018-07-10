package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.MainApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by byang059 on 1/31/18.
 */

@Singleton
@Component(modules = MainApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainApplication application);
}
