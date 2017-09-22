package com.reference.apublic.web;

import android.content.Context;
import android.content.Intent;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.router.PluginService;
import com.module.contract.web.IWeb;
import com.reference.apublic.web.gen.DaoMaster;
import com.reference.apublic.web.gen.SpeedDao;

/**
 * Created by byang059 on 9/15/17.
 */

public class WebPluginService extends PluginService implements IWeb {
    private static WebPluginService instance;

    @Override
    protected void onCreate() {
        super.onCreate();
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
}
