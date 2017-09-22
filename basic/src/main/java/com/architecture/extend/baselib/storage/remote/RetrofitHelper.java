package com.architecture.extend.baselib.storage.remote;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.BuildConfig;
import com.architecture.extend.baselib.config.AppConfig;
import com.architecture.extend.baselib.config.Environment;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by byang059 on 9/22/17.
 */

public class RetrofitHelper {

    private static RetrofitHelper mRetrofitHelper = new RetrofitHelper();
    private Retrofit mRetrofit;
    private HashMap<String, String> mHeaders;

    private RetrofitHelper() {
        mRetrofit = initRetrofit();
    }

    private Retrofit initRetrofit() {
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

        OkHttpClient okHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder().retryOnConnectionFailure(true)
                .cache(new Cache(BaseApplication.getInstance().getCacheDir(),
                        AppConfig.CACHE_MAX_SIZE)).addInterceptor(new TokenInterceptor())
                .addNetworkInterceptor(new CacheControlInterceptor())
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

        return new Retrofit.Builder().baseUrl(Environment.API_BASE_URL).client(okHttpClient)
                .addConverterFactory(gsonConverterFactory).build();
    }

    public static RetrofitHelper getInstance() {
        return mRetrofitHelper;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void addHeader(String headerName, String headerValue) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.put(headerName, headerValue);
    }

}
