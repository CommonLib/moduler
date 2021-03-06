package com.architecture.extend.architecture.dagger;

import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.architecture.MainApiService;
import com.architecture.extend.architecture.MainApplication;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.dagger.BaseApplicationModule;
import com.module.contract.pic.IPicService;
import com.module.contract.web.IWebService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by byang059 on 1/31/18.
 */

@Module(includes = {BaseApplicationModule.class, SubComponentModule.class})
public class MainApplicationModule {

    @Singleton
    @Provides
    public MainApiService provideMainApiService(Retrofit retrofit) {
        return retrofit.create(MainApiService.class);
    }

    @Singleton
    @Provides
    public IPicService provideIPicService(ARouter aRouter) {
        return aRouter.navigation(IPicService.class);
    }

    @Singleton
    @Provides
    public IWebService provideIWebService(ARouter aRouter) {
        return aRouter.navigation(IWebService.class);
    }

    @Singleton
    @Provides
    public MainApplication provideApplication() {
        return (MainApplication) BaseApplication.getInstance();
    }
}
