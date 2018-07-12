package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.MainApplication;
import com.architecture.extend.architecture.MainRepository;
import com.architecture.extend.architecture.SecondRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by byang059 on 1/31/18.
 */

@Singleton
@Component(modules = MainApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainApplication application);
    void inject(MainRepository mainRepository);
    void inject(SecondRepository secondRepository);
}
