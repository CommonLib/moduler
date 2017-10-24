package com.architecture.extend.baselib.base;


import com.architecture.extend.baselib.mvvm.BaseViewModel;

import java.util.HashMap;

/**
 * Created by byang059 on 6/8/17.
 * this shard data scope is activity.
 */

public class SharedViewModel extends BaseViewModel<ShareModel> {

    private HashMap<String, Object> mDataMap;

    public SharedViewModel() {
        mDataMap = new HashMap<>();
    }

    public void put(String key, Object value) {
        mDataMap.put(key, value);
    }

    public void remove(String key) {
        mDataMap.remove(key);
    }

    public Object get(String key) {
        return mDataMap.get(key);
    }
}
