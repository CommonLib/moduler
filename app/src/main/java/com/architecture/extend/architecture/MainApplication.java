package com.architecture.extend.architecture;


import android.os.Handler;

import com.architecture.extend.architecture.dagger.DaggerApplicationComponent;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;


/**
 * Created by byang059 on 12/19/16.
 */

public class MainApplication extends BaseApplication {

    @Inject
    Handler mHandler;

    @Override
    public void onCreate() {
        DaggerApplicationComponent.builder()
                .build().inject(this);
        super.onCreate();

        if (mHandler != null) {
            LogUtil.d("MainApplication inject success" + mHandler);
        }
    }
}

