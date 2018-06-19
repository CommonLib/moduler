package com.architecture.extend.architecture;


import android.os.Handler;

import com.architecture.extend.architecture.dagger.ApplicationComponent;
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
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        mApplicationComponent = DaggerApplicationComponent.builder().build();
        mApplicationComponent.inject(this);
        super.onCreate();

        if (mHandler != null) {
            LogUtil.d("MainApplication inject success" + mHandler);
        }
    }

    public ApplicationComponent getApplicationComponent(){
        return mApplicationComponent;
    }


}

