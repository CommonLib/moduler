package com.architecture.extend.baselib.router;

import java.util.HashMap;

/**
 * Created by byang059 on 9/15/17.
 */

public class Router {
    private static Router sInstance = new Router();
    private HashMap<String, Provider> mProviders = null;

    private Router() {
        mProviders = new HashMap<>();
    }

    public static Router getInstance() {
        return sInstance;
    }

    public void registerProvider(Class<? extends Contract> clazz, Provider provider) {
        mProviders.put(clazz.getName(), provider);
    }

    public Contract service(Class<? extends Contract> clazz) {
        return mProviders.get(clazz.getName());
    }
}
