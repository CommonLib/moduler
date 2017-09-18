package com.architecture.extend.baselib.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by appledev116 on 3/10/16.
 */
public abstract class BaseFragment<VMC> extends Fragment implements ViewLayer {

    private ViewModel mViewModel;
    private boolean mIsForeground;
    private BaseActivity mActivity;
    private int mLayoutId;

    @Override
    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = null;
        if (mLayoutId != -1) {
            contentView = inflater.inflate(mLayoutId, container, false);
        }
        return contentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration configuration = getClass().getAnnotation(Configuration.class);
        mLayoutId = configuration.layout();
        Class<?> aClass = configuration.viewModel();
        mViewModel = ViewModelProviders.getInstance().get(aClass);
        mViewModel.setView(this);
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

    public VMC getViewModel() {
        return (VMC) mViewModel;
    }

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = (ShareDataViewModel) ViewModelProviders
                .getInstance().get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }
}