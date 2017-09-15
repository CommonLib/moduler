package com.architecture.extend.architecture;

import android.arch.lifecycle.LiveData;

import com.architecture.extend.baselib.base.BaseModel;
import com.architecture.extend.baselib.base.ExecuteLiveData;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainModel extends BaseModel<MainContract.ViewModel> implements MainContract.Model{


    @Override
    public LiveData<String> readDatabase(final String a, final String b) {
        ExecuteLiveData<String> data = new ExecuteLiveData<>();
        return data;
    }
}
