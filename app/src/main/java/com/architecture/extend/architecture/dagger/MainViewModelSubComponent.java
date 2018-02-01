package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.MainViewModel;
import com.architecture.extend.baselib.dagger.ViewModelInjectionModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by byang059 on 1/31/18.
 */

@Subcomponent(modules = {ViewModelInjectionModule.class})
public interface MainViewModelSubComponent extends AndroidInjector<MainViewModel>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainViewModel> {
    }
}
