package com.architecture.extend.baselib.router;

import java.util.HashMap;

/**
 * Created by byang059 on 9/15/17.
 */

public class Router {
    private static Router sInstance = null;
    private HashMap<String, Provider> mProviders = null;

    private Router() {
        mProviders = new HashMap<>();
    }

    public static synchronized Router getInstance() {
        if (sInstance == null) {
            sInstance = new Router();
        }
        return sInstance;
    }

    public void registerProvider(String providerName, Provider provider) {
        mProviders.put(providerName, provider);
    }

    public Contract service(Class<? extends Contract> clazz) {
        return mProviders.get(clazz.getName());
    }
}
