package com.architecture.extend.baselib.util;

import com.orhanobut.logger.Logger;

public class LogUtil {

    public static final void e(String log) {
        Logger.e(log);
    }

    public static final void d(String log) {
        Logger.d(log);
    }

    public static final void i(String log) {
        Logger.i(log);
    }

    public static final void v(String log) {
        Logger.v(log);
    }

    public static final void w(String log) {
        Logger.w(log);
    }

    public static final void json(String json) {
        Logger.json(json);
    }

    public static final void exception(Throwable e) {
        Logger.e(e, e.getMessage());
    }

    public static final void d(Object obj) {
        d(obj.toString());
    }
}
