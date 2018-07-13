package com.architecture.extend.architecture;

import android.os.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

/**
 * Created by byang059 on 2018/7/5.
 */
@Singleton
public class SecondRepository {

    @Inject
    Handler mHandler;


    @Inject
    Lazy<MainRepository> mMainRepository;

    @Inject
    public SecondRepository() {
    }
}