package com.architecture.extend.baselib.dagger;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.mvvm.BaseRepository;
import com.architecture.extend.baselib.mvvm.BaseViewModel;

import javax.inject.Provider;

import dagger.android.AndroidInjector;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by byang059 on 2018/7/5.
 */

public class InjectionUtil {

    public static boolean maybeInject(Activity instance, BaseRepository repository) {
        Provider<AndroidInjector.Factory<? extends Activity>> factoryProvider = BaseApplication
                .getInstance().getInjectorActivityFactories().get(instance.getClass());
        if (factoryProvider == null) {
            return false;
        }

        @SuppressWarnings("unchecked") AndroidInjector.Factory<Activity> factory = (AndroidInjector.Factory<Activity>) factoryProvider
                .get();
        AndroidInjector<Activity> injector = checkNotNull(factory.create(instance),
                "%s.create(I) should not return null.", factory.getClass());
        ((Injector) injector).injectRepository(repository);
        return true;
    }

    public static boolean maybeInject(Fragment instance, BaseRepository repository) {
        Provider<AndroidInjector.Factory<? extends Fragment>> factoryProvider = BaseApplication
                .getInstance().getInjectorFragmentFactories().get(instance.getClass());
        if (factoryProvider == null) {
            return false;
        }

        @SuppressWarnings("unchecked") AndroidInjector.Factory<Fragment> factory = (AndroidInjector.Factory<Fragment>) factoryProvider
                .get();
        AndroidInjector<Fragment> injector = checkNotNull(factory.create(instance),
                "%s.create(I) should not return null.", factory.getClass());
        ((Injector) injector).injectViewModel(repository);
        return true;
    }

    public static boolean maybeInject(Activity instance, BaseViewModel viewModel) {
        Provider<AndroidInjector.Factory<? extends Activity>> factoryProvider = BaseApplication
                .getInstance().getInjectorActivityFactories().get(instance.getClass());
        if (factoryProvider == null) {
            return false;
        }

        @SuppressWarnings("unchecked") AndroidInjector.Factory<Activity> factory = (AndroidInjector.Factory<Activity>) factoryProvider
                .get();
        AndroidInjector<Activity> injector = checkNotNull(factory.create(instance),
                "%s.create(I) should not return null.", factory.getClass());
        ((Injector) injector).injectViewModel(viewModel);
        return true;
    }

    public static boolean maybeInject(Fragment instance, BaseViewModel viewModel) {
        Provider<AndroidInjector.Factory<? extends Fragment>> factoryProvider = BaseApplication
                .getInstance().getInjectorFragmentFactories().get(instance.getClass());
        if (factoryProvider == null) {
            return false;
        }

        @SuppressWarnings("unchecked") AndroidInjector.Factory<Fragment> factory = (AndroidInjector.Factory<Fragment>) factoryProvider
                .get();
        AndroidInjector<Fragment> injector = checkNotNull(factory.create(instance),
                "%s.create(I) should not return null.", factory.getClass());
        ((Injector) injector).injectViewModel(viewModel);
        return true;
    }
}
