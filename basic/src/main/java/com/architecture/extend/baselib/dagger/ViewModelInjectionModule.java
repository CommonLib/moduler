package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.mvvm.BaseViewModel;

import java.util.Map;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.multibindings.Multibinds;

/**
 * Created by byang059 on 1/31/18.
 */
@Module(includes = AndroidInjectionModule.class)
public abstract class ViewModelInjectionModule {

    @Multibinds
    abstract Map<Class<? extends BaseViewModel>, AndroidInjector.Factory<? extends
                BaseViewModel>> viewModelInjectorFactories();
}
