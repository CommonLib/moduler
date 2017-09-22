package com.architecture.extend.baselib.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

public class LogUtil {

    public static boolean DEBUG = true;
    public static String tag;

    public static void init(String tag, boolean debug) {
        LogUtil.DEBUG = debug;
        LogUtil.tag = tag;
        Logger.init(tag);
    }


    public static final void e(String log) {
        if (DEBUG)
            Logger.e(log);
    }

    public static final void d(String log) {
        if (DEBUG)
            Log.d(tag, log);
    }

    public static final void i(String log) {
        if (DEBUG)
            Log.i(tag, log);
    }

    public static final void v(String log) {
        if (DEBUG)
            Log.v(tag, log);
    }

    public static final void w(String log) {
        if (DEBUG)
            Logger.w(log);
    }

    public static final void json(String json) {
        if (DEBUG)
            Logger.json(json);
    }

    public static final void exception(Throwable e) {
        if (DEBUG)
            e.printStackTrace();
    }

    public static final void d(Object obj) {
        if (DEBUG)
            d(obj.toString());
    }
}
