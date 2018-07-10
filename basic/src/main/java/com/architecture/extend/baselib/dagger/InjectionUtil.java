package com.architecture.extend.baselib.dagger;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.architecture.extend.baselib.BaseApplication;

import javax.inject.Provider;

import dagger.android.AndroidInjector;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by byang059 on 2018/7/5.
 */

public class InjectionUtil {


    public static AndroidInjector<Activity> inject(Activity instance) {
        Provider<AndroidInjector.Factory<? extends Activity>> factoryProvider = BaseApplication
                .getInstance().getInjectorActivityFactories().get(instance.getClass());
        if (factoryProvider == null) {
            return null;
        }

        @SuppressWarnings("unchecked") AndroidInjector.Factory<Activity> factory = (AndroidInjector.Factory<Activity>) factoryProvider
                .get();
        AndroidInjector<Activity> injector = checkNotNull(factory.create(instance),
                "%s.create(I) should not return null.", factory.getClass());
        injector.inject(instance);
        return injector;
    }

    public static AndroidInjector<Fragment> inject(Fragment instance) {
        Provider<AndroidInjector.Factory<? extends Fragment>> factoryProvider = BaseApplication
                .getInstance().getInjectorFragmentFactories().get(instance.getClass());
        if (factoryProvider == null) {
            return null;
        }

        @SuppressWarnings("unchecked") AndroidInjector.Factory<Fragment> factory = (AndroidInjector.Factory<Fragment>) factoryProvider
                .get();
        AndroidInjector<Fragment> injector = checkNotNull(factory.create(instance),
                "%s.create(I) should not return null.", factory.getClass());
        injector.inject(instance);
        return injector;
    }
}
