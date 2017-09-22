package com.architecture.extend.baselib;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.architecture.extend.baselib.router.PluginService;
import com.architecture.extend.baselib.router.Contract;
import com.architecture.extend.baselib.router.Router;
import com.architecture.extend.baselib.util.LogUtil;

import java.util.List;

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
        String processName = getProcessName(this, android.os.Process.myPid());
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

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * take App versionCode
     *
     * @return
     */
    public int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        int versionCode = -1;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * take App versionName
     *
     * @return
     */
    public String getVersionName() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
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
