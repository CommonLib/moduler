package com.architecture.extend.architecture;

import android.view.View;

import com.architecture.extend.baselib.mvvm.BaseRepository;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;

/**
 * Created by byang059 on 2018/7/5.
 */

public class SecondRepository extends BaseRepository {

    @Inject
    View mView;

    @Inject
    public SecondRepository(SecondActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("SecondActivity inject success" + mView);
    }
}
