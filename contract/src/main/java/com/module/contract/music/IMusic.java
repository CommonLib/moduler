package com.module.contract.music;

import android.content.Context;

import com.architecture.extend.baselib.router.Contract;


/**
 * Created by byang059 on 9/15/17.
 */

public interface IMusic extends Contract {
    boolean playMusic(Context context);
    boolean stopMusic(Context context);
}
