package com.architecture.extend.architecture;

import android.os.Handler;

import com.architecture.extend.baselib.mvvm.BaseRepository;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;

/**
 * Created by byang059 on 2018/7/5.
 */
public class SecondRepository extends BaseRepository {

    @Inject
    Handler mHandler;

    @Inject
    MainRepository mMainRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("mHandler" + mHandler);
        LogUtil.d("mMainRepository" + mMainRepository);
    }
}
