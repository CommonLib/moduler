package com.reference.apublic.web;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.router.PluginService;
import com.module.contract.router.RouterMaps;
import com.module.contract.web.IWebService;
import com.reference.apublic.web.gen.DaoMaster;
import com.reference.apublic.web.gen.SpeedDao;

/**
 * Created by byang059 on 9/15/17.
 */

@Route(path = RouterMaps.Service.WEB)
public class WebPluginServiceImpl extends PluginService implements IWebService {
    private static WebPluginServiceImpl instance;

    @Override
    public void init(Context context) {
        instance = this;
        DaoMaster master = new DaoMaster(
                new DaoMaster.DevOpenHelper(BaseApplication.getInstance(), "user.db")
                        .getWritableDatabase());
        SpeedDao speedDao = master.newSession().getSpeedDao();
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
