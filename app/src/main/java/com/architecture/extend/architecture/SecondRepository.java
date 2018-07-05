package com.architecture.extend.architecture;

import com.architecture.extend.baselib.mvvm.BaseRepository;

import javax.inject.Inject;

/**
 * Created by byang059 on 2018/7/5.
 */

public class SecondRepository extends BaseRepository {

    @Inject
    public SecondRepository(SecondActivity activity) {
        super(activity);
    }
}
