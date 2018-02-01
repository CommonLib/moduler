package com.architecture.extend.architecture;


import com.architecture.extend.architecture.dagger.DaggerApplicationComponent;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.dagger.DaggerBaseApplicationComponent;


/**
 * Created by byang059 on 12/19/16.
 */

public class MainApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder()
                .baseApplicationComponent(DaggerBaseApplicationComponent.create()).build()
                .inject(this);
    }
}

