package com.architecture.extend.baselib.base;

import android.app.Activity;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.architecture.extend.baselib.util.GenericUtil;

/**
 * Created by appledev116 on 3/10/16.
 */
public abstract class BaseFragment<VM extends BaseViewModel, VMC> extends LifecycleFragment
        implements ViewLayer {

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
        mActivity = (BaseActivity)activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<VM> vmType = GenericUtil.getGenericsSuperType(this, 0);
        mViewModel = ViewModelProviders.of(mActivity).get(vmType);
        mViewModel.setView(this);
        getLifecycle().addObserver(mViewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsForeground = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsForeground = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsForeground = false;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    public VMC getViewModel() {
        return (VMC) mViewModel;
    }

    protected <T> T getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders.of(mActivity)
                .get(ShareDataViewModel.class);
        return (T) shareDataViewModel.get(key);
    }
}