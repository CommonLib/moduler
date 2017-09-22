package com.architecture.extend.baselib;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.architecture.extend.baselib.router.Contract;
import com.architecture.extend.baselib.router.PluginService;
import com.architecture.extend.baselib.router.Router;
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
            init();
        }
    }

    private void init() {
        instance = this;
        LogUtil.init(getPackageName(), true);
        mHandler = new Handler();
        pluginComponent();
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

    protected abstract void pluginComponent();

    protected void plugin(Class<? extends Contract> clazz, PluginService pluginService) {
        Router.getInstance().registerProvider(clazz, pluginService);
    }

    protected void plugin(Class<? extends Contract> clazz, String providerPackageName)
            throws Exception {
        Class<?> providerClass = Class.forName(providerPackageName);
        PluginService pluginService = (PluginService) providerClass.newInstance();
        Router.getInstance().registerProvider(clazz, pluginService);
    }
}
