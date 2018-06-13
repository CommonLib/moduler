package com.architecture.extend.architecture;

import com.architecture.extend.baselib.dagger.ActicityScope;
import com.architecture.extend.baselib.dagger.BaseActivityModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by byang059 on 2018/6/13.
 */

@Module(includes = BaseActivityModule.class)
public class ActivityModule {

    @ActicityScope
    @Provides
    Weather provodeWeather() {
        return new Weather();
    }
}
