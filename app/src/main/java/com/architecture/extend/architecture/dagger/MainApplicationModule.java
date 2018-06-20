package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.MainApiService;
import com.architecture.extend.baselib.dagger.ApplicationScope;
import com.architecture.extend.baselib.dagger.BaseApplicationModule;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by byang059 on 1/31/18.
 */

@Module(includes = {BaseApplicationModule.class, SubComponentModule.class})
public class MainApplicationModule {

    @ApplicationScope
    @Provides
    public MainApiService provideMainApiService(Retrofit retrofit){
        return retrofit.create(MainApiService.class);
    }
}
