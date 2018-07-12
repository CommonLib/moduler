package com.architecture.extend.baselib;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.WorkerThread;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by byang059 on 12/19/16.
 */

public class BaseApplication extends MultiDexApplication
        implements HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    Handler mHandler;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Inject
    Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> injectorActivityFactories;

    @Inject
    Map<Class<? extends Fragment>, Provider<AndroidInjector.Factory<? extends Fragment>>> injectorFragmentFactories;

    @Inject
    Executor mExecutor;

    private static BaseApplication instance;
    private static boolean isInited = false;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isInited) {
            init(BuildConfig.DEBUG);
            mExecutor.execute(() -> asyncInit(BuildConfig.DEBUG));
            isInited = true;
        }
    }

    protected void init(boolean debugMode) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(getClass().getSimpleName()).build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        Utils.init(this);
        if (debugMode) {
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.printStackTrace();
            Logger.addLogAdapter(new DiskLogAdapter());
        }
        ARouter.init(this);
    }

    @WorkerThread
    protected void asyncInit(boolean debugMode) {

    }

    public void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void postDelay(Runnable runnable, long millis) {
        mHandler.postDelayed(runnable, millis);
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingFragmentInjector;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    public Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> getInjectorActivityFactories() {
        return injectorActivityFactories;
    }

    public Map<Class<? extends Fragment>, Provider<AndroidInjector.Factory<? extends Fragment>>> getInjectorFragmentFactories() {
        return injectorFragmentFactories;
    }

    public static void setApplication(BaseApplication instance) {
        BaseApplication.instance = instance;
    }
}
