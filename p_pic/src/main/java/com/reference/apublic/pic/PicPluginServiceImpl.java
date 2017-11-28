package com.reference.apublic.pic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.architecture.extend.baselib.router.PluginService;
import com.module.contract.pic.IPicService;
import com.module.contract.router.RouterConstant;


/**
 * Created by byang059 on 9/15/17.
 */

@Route(path = RouterConstant.Pic.SERVICE_PIC)
public class PicPluginServiceImpl extends PluginService implements IPicService {

    @Override
    public boolean playPic(Context context) {
        return false;
    }

    @Override
    public boolean stopPic(Context context) {
        return false;
    }
}
