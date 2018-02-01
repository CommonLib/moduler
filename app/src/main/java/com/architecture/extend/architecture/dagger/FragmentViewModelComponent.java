package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.FragmentViewModel;
import com.architecture.extend.baselib.dagger.ViewModelInjectionModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by byang059 on 2/1/18.
 */

@Subcomponent(modules = {ViewModelInjectionModule.class})
public interface FragmentViewModelComponent extends AndroidInjector<FragmentViewModel>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<FragmentViewModel> {
    }
}
