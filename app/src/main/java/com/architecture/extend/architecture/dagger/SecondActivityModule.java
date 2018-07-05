package com.architecture.extend.architecture.dagger;

import android.view.View;

import com.architecture.extend.architecture.SecondActivity;
import com.architecture.extend.architecture.Weather;
import com.architecture.extend.baselib.dagger.ActivityScope;
import com.architecture.extend.baselib.dagger.BaseActivityModule;
import com.architecture.extend.baselib.mvvm.BaseActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by byang059 on 2018/6/13.
 */

@Module(includes = BaseActivityModule.class)
public abstract class SecondActivityModule {

    @ActivityScope
    @Provides
    static Weather provideWeather() {
        return new Weather();
    }

    @ActivityScope
    @Provides
    static View provideView(SecondActivity mainActivity) {
        return new View(mainActivity);
    }

    @Binds
    abstract BaseActivity provideActivity(SecondActivity mainActivity);
}
