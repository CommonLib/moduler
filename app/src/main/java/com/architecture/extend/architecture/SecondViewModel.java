package com.architecture.extend.architecture;

import android.view.View;

import com.architecture.extend.baselib.mvvm.BaseViewModel;

import javax.inject.Inject;

/**
 * Created by byang059 on 10/24/17.
 */

public class SecondViewModel extends BaseViewModel{
    @Inject
    View mView;

    @Override
    public void onViewCreate() {
        super.onViewCreate();
        System.out.println(mView);
    }
}
