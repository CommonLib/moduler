package com.architecture.extend.baselib.dagger;

import java.util.Map;

import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.Multibinds;

/**
 * Created by byang059 on 1/31/18.
 */
@Module(includes = AndroidSupportInjectionModule.class)
public abstract class ObjectInjectionModule {

    @Multibinds
    abstract Map<Class<? extends Object>, AndroidInjector.Factory<? extends Object>> viewModelInjectorFactories();
}
