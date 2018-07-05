package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.support.annotation.CallSuper;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.dagger.Injector;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.android.InjectAble;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by byang059 on 5/25/17.
 */

public abstract class BaseRepository implements InjectAble {

    @Inject
    Executor mExecutor;

    public BaseRepository(Activity activity) {
        maybeInject(activity);
        onCreate();
    }

    @CallSuper
    public void onCreate() {
    }

    protected void runOnWorkerThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    public boolean maybeInject(Activity instance) {
        Provider<AndroidInjector.Factory<? extends Activity>> factoryProvider = BaseApplication
                .getInstance().getInjectorActivityFactories().get(instance.getClass());
        if (factoryProvider == null) {
            return false;
        }

        @SuppressWarnings("unchecked") AndroidInjector.Factory<Activity> factory = (AndroidInjector.Factory<Activity>) factoryProvider
                .get();
        AndroidInjector<Activity> injector = checkNotNull(factory.create(instance),
                "%s.create(I) should not return null.", factory.getClass());
        ((Injector) injector).injectRepository(this);
        return true;
    }
}
