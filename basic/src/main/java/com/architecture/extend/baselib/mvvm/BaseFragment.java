package com.architecture.extend.baselib.mvvm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.os.MessageQueue;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.base.PermissionCallBack;
import com.architecture.extend.baselib.dagger.InjectionUtil;
import com.architecture.extend.baselib.dagger.Injector;
import com.architecture.extend.baselib.widget.LoadStateView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by burtYang on 10/09/17.
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment
        implements ViewLayer<VM>, MessageQueue.IdleHandler, RequestPermissionAble {

    private VM mViewModel;
    private boolean mIsForeground;
    private boolean mIsDestroyed;
    private BaseActivity mActivity;
    private ViewForegroundSwitchListener mSwitchListener;
    private AndroidInjector<Fragment> mInjector;
    @Inject
    MessageQueue mMessageQueue;
    @Inject
    ConfigureInfo mConfigureInfo;
    private ViewDelegate mViewDelegate;

    @Override
    public void onAttach(Activity activity) {
        mInjector = InjectionUtil.inject(this);
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsForeground = true;
        mViewModel = instanceViewModel(mInjector);
        getLifecycle().addObserver(mViewModel);
        setForegroundSwitchCallBack(mViewModel);
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
        mMessageQueue.addIdleHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ConfigureInfo configureInfo = getConfigureInfo();
        mViewDelegate = new ViewDelegate(this);
        return mViewDelegate
                .initViewFromConfigureInfo(configureInfo, inflater, container, mActivity,
                        getLayoutId());
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsForeground = true;
        mIsDestroyed = false;
        mSwitchListener.onViewForeground();
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsForeground = false;
        mSwitchListener.onViewBackground();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsDestroyed = true;
        getLifecycle().removeObserver(mViewModel);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            mIsForeground = false;
        } else {
            mIsForeground = true;
        }
    }

    protected abstract @LayoutRes
    int getLayoutId();

    protected void handleArguments(@NonNull Bundle arguments) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }

    /**
     * for android verision above 23 to apply permissions
     *
     * @param permissions
     * @param callBack
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void usePermission(PermissionCallBack callBack, String... permissions) {
        mActivity.usePermission(callBack, permissions);
    }

    @Override
    public boolean isForeground() {
        return mIsForeground;
    }

    @Override
    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public VM getViewModel() {
        return mViewModel;
    }

    @Override
    public void setForegroundSwitchCallBack(ViewForegroundSwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

    @Override
    public ConfigureInfo getConfigureInfo() {
        return mConfigureInfo;
    }

    public VM instanceViewModel(AndroidInjector<Fragment> injector) {
        VM viewModel = ViewModelProviders.of(this).get(getViewModelClass());
        if (injector instanceof Injector) {
            ((Injector) injector).injectViewModel(viewModel);
        }
        return viewModel;
    }

    public void startPage(Bundle bundle, String path) {
        mViewModel.startPage(bundle, path);
    }

    public void startPageForResult(Bundle bundle, String path, int requestCode) {
        mViewModel.startPageForResult(bundle, path, mActivity, requestCode);
    }

    protected PtrFrameLayout getPullToRefreshView() {
        return mViewDelegate.getPullToRefreshView();
    }

    protected LoadStateView getLoadStateView() {
        return mViewDelegate.getLoadStateView();
    }

    @Override
    public BaseActivity getBindActivity() {
        return mActivity;
    }

    @Override
    public boolean queueIdle() {
        return false;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mActivity.getRxPermissions();
    }

    @Override
    public void attachContentView(ViewGroup viewGroup, View content) {
        viewGroup.addView(content);
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    @Override
    public ViewDelegate getViewDelegate() {
        return mViewDelegate;
    }
}