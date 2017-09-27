package com.architecture.extend.architecture;

import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.architecture.extend.baselib.mvvm.LiveData;
import com.architecture.extend.baselib.mvvm.LiveViewModelCallBack;
import com.architecture.extend.baselib.util.LogUtil;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel<MainModel> {

    private LiveData<String> mStringLiveData;

    public LiveData<String> getUserString() {
        LiveData<String> data = getModel().readDatabase("a", "b");
        data.intercept(new LiveViewModelCallBack<String>() {
            @Override
            public String onComplete(String value) {
                LogUtil.d("viewModel onComplete");
                return value + " viewmodel";
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("viewModel onStart");
            }

            @Override
            public int onProgressUpdate(int progress) {
                LogUtil.d("viewModel onProgressUpdate progress = " + progress);
                return progress + 10;
            }

            @Override
            public String onCacheReturn(String cache) {
                LogUtil.d("viewModel onCacheReturn");
                return cache + "viewmodel";
            }

            @Override
            public Throwable onError(Throwable error) {
                LogUtil.d("viewModel onError");
                return new Exception("viewmodel", error);
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
