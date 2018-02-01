package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.mvvm.BaseViewModel;

import dagger.android.AndroidInjector;

/**
 * Created by byang059 on 1/31/18.
 */

public interface HasViewModelInjector {
    AndroidInjector<BaseViewModel> viewModelInjector();
}
