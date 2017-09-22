package com.architecture.extend.architecture;


import com.architecture.extend.baselib.BaseApplication;
import com.module.contract.pic.IPic;
import com.module.contract.web.IWeb;

/**
 * Created by byang059 on 12/19/16.
 */

public class MainApplication extends BaseApplication {


    @Override
    protected void pluginComponent() {
        try {
            plugin(IWeb.class, "com.reference.apublic.web.WebPluginService");
            plugin(IPic.class, "com.reference.apublic.pic.PicPluginService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
