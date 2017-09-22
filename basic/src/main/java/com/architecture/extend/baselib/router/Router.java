package com.architecture.extend.baselib.router;

import java.util.HashMap;

/**
 * Created by byang059 on 9/15/17.
 */

public class Router {
    private static Router sInstance = new Router();
    private HashMap<String, PluginService> mProviders = null;

    private Router() {
        mProviders = new HashMap<>();
    }

    public static Router getInstance() {
        return sInstance;
    }

    public void registerProvider(Class<? extends Contract> clazz, PluginService pluginService) {
        mProviders.put(clazz.getName(), pluginService);
    }

    public Contract service(Class<? extends Contract> clazz) {
        return mProviders.get(clazz.getName());
    }
}
