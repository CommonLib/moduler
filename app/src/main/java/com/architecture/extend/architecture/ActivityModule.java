package com.architecture.extend.architecture;

import android.view.View;

import com.architecture.extend.baselib.dagger.ActicityScope;
import com.architecture.extend.baselib.dagger.BaseActivityModule;
import com.architecture.extend.baselib.mvvm.BaseActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by byang059 on 2018/6/13.
 */

@Module(includes = BaseActivityModule.class)
public abstract class ActivityModule {

    @ActicityScope
    @Provides
    static Weather provodeWeather() {
        return new Weather();
    }

    @ActicityScope
    @Provides
    static View provodeView(SecondActivity mainActivity) {
        return new View(mainActivity);
    }

    @Binds
    abstract BaseActivity provideActivity(SecondActivity mainActivity);
}
