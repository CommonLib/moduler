package com.reference.apublic.web;

import android.content.Context;
import android.content.Intent;

import com.architecture.extend.baselib.router.Provider;
import com.module.contract.web.IWeb;

/**
 * Created by byang059 on 9/15/17.
 */

public class WebProvider extends Provider implements IWeb {

    @Override
    public void openWeb(Context context) {
        context.startActivity(new Intent(context, WebActivity.class));
    }
}
