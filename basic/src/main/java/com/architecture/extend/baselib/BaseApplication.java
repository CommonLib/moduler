package com.architecture.extend.baselib;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.WorkerThread;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.baselib.dagger.HasObjectInjector;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by byang059 on 12/19/16.
 */

public class BaseApplication extends MultiDexApplication
        implements HasActivityInjector, HasObjectInjector, HasSupportFragmentInjector {

    @Inject
    Handler mHandler;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Inject
    DispatchingAndroidInjector<Object> dispatchingViewModelInjector;

    private static BaseApplication instance;
    private static boolean isInit = false;

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
        if (!isInit) {
            init(BuildConfig.DEBUG);
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    asyncInit(BuildConfig.DEBUG);
                }
            });
            isInit = true;
        }

        if(mHandler != null){
            LogUtil.d("BaseApplication inject success");
        }
    }

    protected void init(final boolean debugMode) {
        instance = this;
        LogUtil.init(getClass().getSimpleName(), debugMode);
        if (debugMode) {
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.printStackTrace();
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

    @Override
    public AndroidInjector<Object> objectInjector() {
        return dispatchingViewModelInjector;
    }
}
