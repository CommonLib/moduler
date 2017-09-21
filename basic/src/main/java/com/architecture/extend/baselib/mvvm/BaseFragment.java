package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.base.ShareDataViewModel;
import com.architecture.extend.baselib.util.GenericUtil;

/**
 * Created by appledev116 on 3/10/16.
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment implements ViewAble{

    private VM mViewModel;
    private boolean mIsForeground;
    private BaseActivity mActivity;
    private ViewForegroundSwitchListener mSwitchListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<VM> viewModelClazz = GenericUtil.getGenericsSuperType(this.getClass(), 0);
        mViewModel = ViewModelProviders.getInstance().get(viewModelClazz);
        mViewModel.onViewCreate();
        setForegroundSwitchCallBack(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
        View contentView = inflater.inflate(getLayoutId(), container, false);
        initView();
        initData();
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsForeground = true;
        mViewModel.onViewStart();
        mSwitchListener.onViewForeground();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onViewResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.onViewPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsForeground = false;
        mViewModel.onViewStop();
        mSwitchListener.onViewBackground();
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

    protected abstract void initData();

    protected abstract void initView();

    protected abstract @LayoutRes int getLayoutId();

    protected void handleArguments(@NonNull Bundle arguments) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
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

    @Override
    public void setForegroundSwitchCallBack(ViewForegroundSwitchListener switchListener) {
        mSwitchListener = switchListener;
    }
}