package com.architecture.extend.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.architecture.extend.baselib.base.BaseViewModel;
import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel<MainContract.View, MainModel, MainContract.Model>
        implements MainContract.ViewModel {

    MediatorLiveData<String> mUserData = new MediatorLiveData<>();

    @Override
    public LiveData<String> getUserString() {
        LiveData<String> stringObservable = getModel().readDatabase("a","b");
        LogUtil.d("LiveData<String> get");
        stringObservable.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                LogUtil.d("viewModel readDatabase =>  current "
                        + "Thread" + Thread.currentThread().getName());
            }
        });
        return mUserData;
    }
}
