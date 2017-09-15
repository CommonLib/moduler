package com.module.contract.pic;

import android.content.Context;

import com.architecture.extend.baselib.router.Contract;


/**
 * Created by byang059 on 9/15/17.
 */

public interface IPic extends Contract {
    boolean playPic(Context context);
    boolean stopPic(Context context);
}
