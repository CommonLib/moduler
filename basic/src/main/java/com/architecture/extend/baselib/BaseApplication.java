package com.architecture.extend.baselib;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 12/19/16.
 */

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;
    private Handler mHandler;
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
            isInited = true;
        }
    }

    private void init(boolean debugMode) {
        instance = this;
        mHandler = new Handler();
        LogUtil.init(getClass().getSimpleName(), true);
        if (debugMode) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
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
}
