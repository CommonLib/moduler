package com.reference.apublic.pic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.architecture.extend.baselib.router.PluginService;
import com.module.contract.pic.IPicService;
import com.module.contract.router.RouterConstants;


/**
 * Created by byang059 on 9/15/17.
 */

@Route(path = RouterConstants.Pic.SERVICE_PIC)
public class PicPluginServiceImpl extends PluginService implements IPicService {

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public boolean playPic(Context context) {
        return false;
    }

    @Override
    public boolean stopPic(Context context) {
        return false;
    }
}
