package com.architecture.extend.architecture;


import com.architecture.extend.baselib.BaseApplication;
import com.module.contract.pic.IPic;
import com.module.contract.web.IWeb;

/**
 * Created by byang059 on 12/19/16.
 */

public class MainApplication extends BaseApplication {


    @Override
    protected void pluginProviders() {
        try {
            plugin(IWeb.class, "com.reference.apublic.web.WebProvider");
            plugin(IPic.class, "com.reference.apublic.pic.PicProvider");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
