package com.reference.apublic.web;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.architecture.extend.baselib.router.PluginService;
import com.module.contract.router.RouterMaps;
import com.module.contract.web.IWebService;

/**
 * Created by byang059 on 9/15/17.
 */

@Route(path = RouterMaps.Service.WEB)
public class WebPluginServiceImpl extends PluginService implements IWebService {

    @Override
    public void init(Context context) {
    }

    @Override
    public void openWeb(Context context) {
        context.startActivity(new Intent(context, WebActivity.class));
    }

    @Override
    public String dealString(String a) {
        return a + " dealed";
    }
}
