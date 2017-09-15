package com.architecture.extend.architecture;

import com.architecture.extend.baselib.BaseApplication;
import com.module.contract.pic.IPic;
import com.module.contract.web.IWeb;
import com.reference.apublic.pic.PicProvider;
import com.reference.apublic.web.WebProvider;

/**
 * Created by byang059 on 12/19/16.
 */

public class MainApplication extends BaseApplication {


    @Override
    protected void pluginProviders() {
        plugin(IWeb.class, new WebProvider());
        plugin(IPic.class, new PicProvider());
    }
}
