package com.architecture.extend.baselib.dagger;

import android.app.Application;

import com.architecture.extend.baselib.mvvm.BaseViewModel;

import dagger.android.AndroidInjector;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by byang059 on 1/31/18.
 */

public class ViewModelInjection {

    public static void inject(BaseViewModel viewModel) {
        checkNotNull(viewModel, "viewModel");
        Application application = viewModel.getApplication();
        if (!(application instanceof HasViewModelInjector)) {
            throw new RuntimeException(String.format("%s does not implement %s",
                    application.getClass().getCanonicalName(),
                    HasViewModelInjector.class.getCanonicalName()));
        }

        AndroidInjector<BaseViewModel> viewModelInjector =
                ((HasViewModelInjector) application).viewModelInjector();
        checkNotNull(viewModelInjector, "%s.viewModelInjector() returned null", application.getClass());
        viewModelInjector.inject(viewModel);
    }
}
