package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.mvvm.BaseViewModel;

import dagger.Component;

/**
 * Created by byang059 on 1/26/18.
 */

@Component
public interface ViewModelComponent {
    void inject(BaseViewModel viewModel);
}
