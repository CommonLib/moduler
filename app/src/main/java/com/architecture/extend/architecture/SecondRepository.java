package com.architecture.extend.architecture;

import android.os.Handler;

import com.architecture.extend.baselib.mvvm.BaseRepository;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by byang059 on 2018/7/5.
 */
@Singleton
public class SecondRepository extends BaseRepository {

    @Inject
    Handler mHandler;

    @Inject
    public SecondRepository() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("mHandler" + mHandler);
    }
}
