package com.architecture.extend.baselib.base.mvvm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.architecture.extend.baselib.base.ShareDataViewModel;
import com.architecture.extend.baselib.util.GenericUtil;

/**
 * Created by appledev116 on 3/10/16.
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment implements ViewLayer {

    private VM mViewModel;
    private boolean mIsForeground;
    private BaseActivity mActivity;

    @Override
    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<VM> viewModelClazz = GenericUtil.getGenericsSuperType(this, 0);
        mViewModel = ViewModelProviders.getInstance().get(viewModelClazz);
        mViewModel.onViewCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsForeground = false;
        mViewModel.onViewStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsForeground = true;
        mViewModel.onViewResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsForeground = false;
        mViewModel.onViewPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.onViewStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.onViewDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            mIsForeground = false;
        } else {
            mIsForeground = true;
        }
    }

    @Override
    public boolean isForeground() {
        return mIsForeground;
    }

    public boolean onBackPressed() {
        return false;
    }

    public VM getViewModel() {
        return mViewModel;
    }

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders.getInstance()
                .get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }
}