package com.architecture.extend.architecture;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.architecture.extend.baselib.mvvm.BaseViewModel;
import com.architecture.extend.baselib.mvvm.LiveData;
import com.architecture.extend.baselib.mvvm.LiveViewModelCallBack;
import com.architecture.extend.baselib.util.LogUtil;
import com.module.contract.pic.IPicService;
import com.module.contract.web.IWebService;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainViewModel extends BaseViewModel<MainModel> {

    @Autowired
    public IWebService webService;
    @Autowired
    public IPicService picService;

    private LiveData<String> mStringLiveData;

    public LiveData<String> getUserString() {
        if (mStringLiveData != null) {
            return mStringLiveData;
        }
        mStringLiveData = new LiveData<>();
        getModel().readDatabase("a", "b", mStringLiveData);
        mStringLiveData.intercept(new LiveViewModelCallBack<String>() {
            @Override
            public String onComplete(String value) {
                LogUtil.d("viewModel onComplete " + value);
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
        shareData("share", mStringLiveData);
        return mStringLiveData;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        String autowired = webService.dealString("autowired");
        picService.playPic(null);
        LogUtil.d(autowired);
    }
}
