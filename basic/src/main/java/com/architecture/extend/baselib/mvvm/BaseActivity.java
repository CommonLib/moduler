package com.architecture.extend.baselib.mvvm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.MessageQueue;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.base.PermissionCallBack;
import com.architecture.extend.baselib.dagger.InjectionUtil;
import com.architecture.extend.baselib.dagger.Injector;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.widget.LoadStateView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Created by byang059 on 12/19/16.
 */

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity
        implements ViewLayer, MessageQueue.IdleHandler, RequestPermissionAble {

    private VM mViewModel;
    private boolean mIsForeground;
    private ViewForegroundSwitchListener mSwitchListener;
    private RxPermissions mRxPermissions;

    @Inject
    public ConfigureInfo injectConfigureInfo;

    @Inject
    MessageQueue mMessageQueue;
    private ViewDelegate mViewDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjector<Activity> injector = InjectionUtil.inject(this);
        super.onCreate(savedInstanceState);
        mIsForeground = true;
        mViewModel = instanceViewModel(injector);
        getLifecycle().addObserver(mViewModel);
        setForegroundSwitchCallBack(mViewModel);
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
        ConfigureInfo configureInfo = getConfigureInfo();
        ViewGroup parent = findViewById(android.R.id.content);
        mViewDelegate = new ViewDelegate(this);
        mViewDelegate.initViewFromConfigureInfo(configureInfo, getLayoutInflater(), parent, this,
                getLayoutId());
        mMessageQueue.addIdleHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsForeground = true;
        mSwitchListener.onViewForeground();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsForeground = false;
        mSwitchListener.onViewBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(mViewModel);
    }

    @Override
    public VM getViewModel() {
        return mViewModel;
    }

    @Override
    public boolean isForeground() {
        return mIsForeground;
    }

    @Override
    public void setForegroundSwitchCallBack(ViewForegroundSwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

    /**
     * for android version above 23 to apply permissions
     *
     * @param permissions
     * @param callBack
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void usePermission(final PermissionCallBack callBack,
                                 @RequiresPermission String... permissions) {
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(this);
        }
        mRxPermissions.requestEach(permissions).subscribe(permission -> {
            if (permission.granted) {
                callBack.onGranted(permission.name);
            } else {
                callBack.onDenied(permission.name);
            }
        });
    }

    @Override
    public RxPermissions getRxPermissions() {
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(this);
        }
        return mRxPermissions;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public <T> T instanceViewModel(AndroidInjector<Activity> injector) {
        Class viewModelClazz = GenericUtil.getGenericsSuperType(this, 0);
        Object viewModel = ViewModelProviders.of(this).get(viewModelClazz);
        if (injector instanceof Injector) {
            ((Injector) injector).injectViewModel(viewModel);
        }
        return (T) viewModel;
    }

    protected abstract @LayoutRes
    int getLayoutId();

    protected void handleIntent(@NonNull Intent intent) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public ConfigureInfo getConfigureInfo() {
        return injectConfigureInfo;
    }

    public void startPage(Bundle bundle, String path) {
        mViewModel.startPage(bundle, path);
    }

    public void startPageForResult(Bundle bundle, String path, int requestCode) {
        mViewModel.startPageForResult(bundle, path, this, requestCode);
    }

    protected void onPullRefreshBegin(PtrFrameLayout frame) {
    }

    public PtrFrameLayout getPullToRefreshView() {
        return mViewDelegate.getPullToRefreshView();
    }

    public LoadStateView getLoadStateView() {
        return mViewDelegate.getLoadStateView();
    }

    public Toolbar getToolbar() {
        return mViewDelegate.getToolbar();
    }

    @Override
    public boolean queueIdle() {
        return false;
    }

    @Override
    public void attachContentView(ViewGroup viewGroup, View content) {
        setContentView(content);
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    @Override
    public BaseActivity getBindActivity() {
        return this;
    }

    @Override
    public ViewDelegate getViewDelegate() {
        return mViewDelegate;
    }
}
