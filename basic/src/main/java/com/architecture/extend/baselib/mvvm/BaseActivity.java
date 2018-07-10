package com.architecture.extend.baselib.mvvm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.base.PermissionCallBack;
import com.architecture.extend.baselib.dagger.InjectionUtil;
import com.architecture.extend.baselib.dagger.Injector;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.ViewUtil;
import com.architecture.extend.baselib.widget.LoadStateView;
import com.blankj.utilcode.util.SizeUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;


/**
 * Created by byang059 on 12/19/16.
 */

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity
        implements Viewable {

    private VM mViewModel;
    private boolean mIsForeground;
    private ViewForegroundSwitchListener mSwitchListener;
    private ConfigureInfo mConfigureInfo;
    private PtrFrameLayout mPullToRefreshView;
    private LoadStateView mLoadStateView;
    private Toolbar mToolbar;
    private RxPermissions mRxPermissions;

    @Inject
    public ConfigureInfo injectConfigureInfo;
    private AndroidInjector<Activity> mInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInjector = InjectionUtil.inject(this);
        super.onCreate(savedInstanceState);
        mIsForeground = true;
        Class<VM> viewModelClazz = GenericUtil.getGenericsSuperType(this, 0);
        mViewModel = ViewModelProviders.of(this).get(viewModelClazz);
        if(mInjector instanceof Injector){
            ((Injector) mInjector).injectViewModel(mViewModel);
        }
        getLifecycle().addObserver(mViewModel);
        setForegroundSwitchCallBack(mViewModel);
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
        mConfigureInfo = getConfigureInfo();
        inflateLayout(mConfigureInfo.isAsyncInflate());
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

    private void inflateLayout(boolean isAsyncInflate) {
        int layoutId = getLayoutId();
        if (layoutId <= 0) {
            return;
        }
        ViewGroup parent = findViewById(android.R.id.content);
        if (isAsyncInflate) {
            LiveData<View> inflate = mViewModel.asyncInflate(layoutId, getLayoutInflater(), parent);
            inflate.observe(this, view -> init(DataBindingUtil.bind(view)));
        } else {
            init(DataBindingUtil.inflate(getLayoutInflater(), layoutId, parent, false));
        }
    }

    private void init(ViewDataBinding binding) {
        setContentView(packageContentView(binding.getRoot()));
        initView(binding);
        initData();
    }

    private void initToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //enable back button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public View packageContentView(View view) {
        View contentView = view;
        if (mConfigureInfo.isLoadingState()) {
            mLoadStateView = ViewUtil.addLoadingStateView(contentView);
            initLoadingStateView(mLoadStateView);
            contentView = mLoadStateView;
        }

        if (mConfigureInfo.isPullToRefresh()) {
            mPullToRefreshView = ViewUtil.addPullToRefreshView(contentView);
            initPullRefreshView(mPullToRefreshView);
            contentView = mPullToRefreshView;
        }

        Boolean enableToolbar = mConfigureInfo.isEnableToolbar();
        if (enableToolbar != null && enableToolbar) {
            contentView = ViewUtil.addToolBarView(this, R.layout.view_tool_bar, contentView);
            mToolbar = contentView.findViewById(R.id.common_tl_toolbar);
            initToolBar(mToolbar);
        }
        return contentView;
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

    protected abstract void initData();

    protected abstract void initView(ViewDataBinding dataBinding);

    protected abstract @LayoutRes
    int getLayoutId();

    protected void handleIntent(@NonNull Intent intent) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }


    public ConfigureInfo getConfigureInfo() {
        return injectConfigureInfo;
    }

    protected void initPullRefreshView(PtrFrameLayout refreshView) {
        MaterialHeader header = new MaterialHeader(this);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, SizeUtils.dp2px(15), 0, SizeUtils.dp2px(10));
        header.setPtrFrameLayout(refreshView);
        refreshView.setHeaderView(header);
        refreshView.addPtrUIHandler(header);
        refreshView.setPinContent(true);
        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onPullRefreshBegin(frame);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                View scrollView = content.findViewById(R.id.view_scroll_content);
                if (scrollView != null) {
                    content = scrollView;
                }
                return super.checkCanDoRefresh(frame, content, header);
            }
        });
    }

    protected void initLoadingStateView(LoadStateView loadStateView) {
        DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.view_loading_state, loadStateView,
                        true);
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
        return mPullToRefreshView;
    }

    public LoadStateView getLoadStateView() {
        return mLoadStateView;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public AndroidInjector<Activity> getInjector() {
        return mInjector;
    }
}
