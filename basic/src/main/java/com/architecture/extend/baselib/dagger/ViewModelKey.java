package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.mvvm.BaseViewModel;

import dagger.MapKey;

/**
 * Created by byang059 on 2/1/18.
 */

@MapKey
public @interface ViewModelKey {
    Class<? extends BaseViewModel> value();
}
