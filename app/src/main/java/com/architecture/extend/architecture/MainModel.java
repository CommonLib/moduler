package com.architecture.extend.architecture;

import com.architecture.extend.baselib.base.BaseModel;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainModel extends BaseModel<MainContract.ViewModel> implements MainContract.Model {

    @Override
    public String readDatabase(String a, String b) {
        return a + b;
    }
}
