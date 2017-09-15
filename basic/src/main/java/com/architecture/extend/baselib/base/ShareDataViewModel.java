package com.architecture.extend.baselib.base;

import android.arch.lifecycle.ViewModel;

import java.util.HashMap;

/**
 * Created by byang059 on 6/8/17.
 * this shard data scope is activity.
 */

public class ShareDataViewModel extends ViewModel {

    private HashMap<String, Object> mShareData;

    public ShareDataViewModel() {
        mShareData = new HashMap<>();
    }

    public void put(String key, Object value){
        mShareData.put(key, value);
    }

    public Object get(String key){
        return mShareData.get(key);
    }
}
