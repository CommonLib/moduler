package com.architecture.extend.architecture;

import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.architecture.extend.baselib.mvvm.LiveData;
import com.architecture.extend.baselib.mvvm.ViewModelCallBack;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel<MainModel> {

    private LiveData<String> mStringLiveData;

    public LiveData<String> getUserString() {
        LiveData<String> data = getModel().readDatabase("a", "b");
        data.intercept(new ViewModelCallBack<String>() {
            @Override
            public String onInterceptData(String s) {
                return s + " viewmodel";
            }
        });
        return data;
    }

    @Override
    public void onViewCreate() {
        super.onViewCreate();
        shareData("abc", "sharted abc");
    }
}
