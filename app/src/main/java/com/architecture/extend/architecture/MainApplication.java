package com.architecture.extend.architecture;


import android.app.Activity;

import com.architecture.extend.architecture.dagger.DaggerApplicationComponent;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.dagger.HasViewModelInjector;
import com.architecture.extend.baselib.mvvm.BaseViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by byang059 on 12/19/16.
 */

public class MainApplication extends BaseApplication implements HasActivityInjector,HasViewModelInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<BaseViewModel> dispatchingViewModelInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder().build().inject(this);
    }

    @Override
    public AndroidInjector<BaseViewModel> viewModelInjector() {
        return dispatchingViewModelInjector;
    }
}

