package com.architecture.extend.baselib;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.baselib.util.AppUtil;
import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 12/19/16.
 */

public abstract class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;
    private Handler mHandler;

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
        String processName = AppUtil.getProcessName(this, android.os.Process.myPid());
        if (TextUtils.equals(processName, getPackageName())) {
            init(BuildConfig.DEBUG);
        }
    }

    private void init(boolean debugMode) {
        instance = this;
        mHandler = new Handler();
        LogUtil.init(getPackageName(), true);
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

    public Handler getHandler(){
        return mHandler;
    }
}
