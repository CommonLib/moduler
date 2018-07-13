package com.architecture.extend.architecture;


import com.architecture.extend.architecture.dagger.ApplicationComponent;
import com.architecture.extend.architecture.dagger.DaggerApplicationComponent;
import com.architecture.extend.baselib.BaseApplication;

import javax.inject.Inject;


/**
 * Created by byang059 on 12/19/16.
 */

public class MainApplication extends BaseApplication {

    @Inject
    SecondRepository mSecondRepository;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        setApplication(this);
        mApplicationComponent = DaggerApplicationComponent.builder().build();
        mApplicationComponent.inject(this);
        super.onCreate();
    }

    public ApplicationComponent getApplicationComponent(){
        return mApplicationComponent;
    }
}

