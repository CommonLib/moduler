package com.architecture.extend.baselib.router;

import android.content.Context;

import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 9/15/17.
 */

public abstract class PluginService implements Contract{
    @Override
    public void init(Context context) {
        LogUtil.d("PluginService_init()");
    }
}