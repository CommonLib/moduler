package com.reference.apublic.pic;

import android.content.Context;
import android.content.Intent;

import com.architecture.extend.baselib.router.Provider;
import com.module.contract.pic.IPic;


/**
 * Created by byang059 on 9/15/17.
 */

public class PicProvider extends Provider implements IPic {

    @Override
    public boolean playPic(Context context) {
        context.startActivity(new Intent(context, PicActivity.class));
        return false;
    }

    @Override
    public boolean stopPic(Context context) {
        return false;
    }
}
