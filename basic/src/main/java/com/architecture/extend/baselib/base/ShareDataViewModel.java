package com.architecture.extend.baselib.base;


import com.architecture.extend.baselib.base.mvvm.BaseViewModel;

import java.util.HashMap;

/**
 * Created by byang059 on 6/8/17.
 * this shard data scope is activity.
 */

public class ShareDataViewModel extends BaseViewModel {

    private HashMap<String, Object> mShareData;

    public ShareDataViewModel() {
        mShareData = new HashMap<>();
    }

    public void put(String key, Object value) {
        mShareData.put(key, value);
    }

    public Object take(String key) {
        Object value = mShareData.get(key);
        if (value != null) {
            mShareData.remove(key);
        }
        return value;
    }

    public Object get(String key) {
        return mShareData.get(key);
    }
}
