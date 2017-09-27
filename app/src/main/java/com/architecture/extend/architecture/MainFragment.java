package com.architecture.extend.architecture;

import android.databinding.ViewDataBinding;

import com.architecture.extend.baselib.mvvm.BaseFragment;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;

/**
 * Created by byang059 on 9/27/17.
 */

public class MainFragment extends BaseFragment<MainViewModel> {

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(ViewDataBinding binding) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public ConfigureInfo getConfigureInfo() {
        return new ConfigureInfo.Builder().asyncInflate(true).loadingState(true).pullToRefresh
                (true).toolbar(true).build();
    }
}
