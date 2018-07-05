package com.architecture.extend.baselib.dagger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.BuildConfig;
import com.architecture.extend.baselib.config.AppConfig;
import com.architecture.extend.baselib.config.Environment;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;
import com.architecture.extend.baselib.storage.local.ACache;
import com.architecture.extend.baselib.storage.remote.HeaderInterceptor;
import com.architecture.extend.baselib.storage.remote.Ignore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.android.support.AndroidSupportInjectionModule;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by byang059 on 2/1/18.
 */

@Module(includes = AndroidSupportInjectionModule.class)
public class BaseApplicationModule {

    @ApplicationScope
    @Provides
    public ConfigureInfo provideConfigureInfo() {
        return ConfigureInfo.defaultConfigure();
    }

    @ApplicationScope
    @Provides
    public Handler provideHandler() {
        return new Handler();
    }

    @ApplicationScope
    @Provides
    public BaseApplication provideApplication() {
        return BaseApplication.getInstance();
    }

    @ApplicationScope
    @Provides
    public Executor provideExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    @ApplicationScope
    @Provides
    public ACache provideAcache(BaseApplication baseApplication) {
        return ACache.get(baseApplication);
    }

    @ApplicationScope
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if (f.getAnnotation(Ignore.class) != null) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                if (clazz.getAnnotation(Ignore.class) != null) {
                    return true;
                }
                return false;
            }
        });

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory
                .create(gsonBuilder.create());

        return new Retrofit.Builder().baseUrl(Environment.API_BASE_URL).client(okHttpClient)
                .addConverterFactory(gsonConverterFactory).build();
    }

    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClient(HeaderInterceptor headerInterceptor) {
        OkHttpClient okHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder().retryOnConnectionFailure(true)
                .cache(new Cache(BaseApplication.getInstance().getCacheDir(),
                        AppConfig.HTTP_CACHE_MAX_SIZE)).addInterceptor(headerInterceptor)
                .connectTimeout(AppConfig.API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(AppConfig.API_WRITE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(AppConfig.API_WRITE_READ_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = builder.addInterceptor(loggingInterceptor).build();
        } else {
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    @ApplicationScope
    @Provides
    @Named("launcher")
    public Intent provideLauncherIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        return intent;
    }

    @ApplicationScope
    @Provides
    public ARouter provideARounter() {
        return ARouter.getInstance();
    }
}