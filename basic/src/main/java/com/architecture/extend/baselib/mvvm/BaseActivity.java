package com.architecture.extend.baselib.mvvm;

import android.annotation.TargetApi;
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
import com.architecture.extend.baselib.base.ShareDataViewModel;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.PermissionAccessUtil;
import com.architecture.extend.baselib.util.ViewUtil;
import com.architecture.extend.baselib.widget.LoadStateView;
import com.github.kayvannj.permission_utils.PermissionUtil;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;


/**
 * Created by byang059 on 12/19/16.
 */

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity
        implements ViewAble {

    private VM mViewModel;
    private boolean mIsForeground;
    private ViewForegroundSwitchListener mSwitchListener;
    private ArrayList<PermissionUtil.PermissionRequestObject> mPermissionRequests;
    private ConfigureInfo mConfigureInfo;
    private PtrFrameLayout mPullToRefreshView;
    private LoadStateView mLoadStateView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<VM> viewModelClazz = GenericUtil.getGenericsSuperType(this, 0);
        mViewModel = ViewModelProviders.getInstance().get(viewModelClazz);
        mViewModel.onViewCreate();
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
        mViewModel.onViewStart();
        mSwitchListener.onViewForeground();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onViewPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mViewModel.onViewRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsForeground = false;
        mViewModel.onViewStop();
        mSwitchListener.onViewBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onViewDestroy();
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
     * for android version above 23 to apply permission
     * TODO need to impl apply for mulit permission once
     *
     * @param permission
     * @param callBack
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void usePermission(@RequiresPermission String permission,
                                 PermissionCallBack callBack) {
        if (PermissionAccessUtil.hasPermission(this, permission)) {
            callBack.onGranted(permission);
            return;
        }
        PermissionUtil.PermissionRequestObject permissionRequest = PermissionAccessUtil
                .requestPermission(this, permission, callBack);
        if (mPermissionRequests == null) {
            mPermissionRequests = new ArrayList<>();
        }
        mPermissionRequests.add(permissionRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (mPermissionRequests != null && mPermissionRequests.size() > 0) {
            for (PermissionUtil.PermissionRequestObject permissionRequest : mPermissionRequests) {
                permissionRequest
                        .onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            mPermissionRequests.clear();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void inflateLayout(boolean isAsyncInflate) {
        int layoutId = getLayoutId();
        if (layoutId <= 0) {
            return;
        }
        ViewGroup parent = (ViewGroup) findViewById(android.R.id.content);
        if (isAsyncInflate) {
            LiveData<View> inflate = getViewModel()
                    .asyncInflate(layoutId, getLayoutInflater(), parent);
            inflate.subscribe(this, new LiveCallBack<View>() {
                @Override
                public void onComplete(View view) {
                    init(DataBindingUtil.bind(view));
                }
            });
        } else {
            init(DataBindingUtil.inflate(getLayoutInflater(), layoutId, parent, false));
        }
    }

    public void init(ViewDataBinding binding) {
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
            mToolbar = (Toolbar) contentView.findViewById(R.id.common_tl_toolbar);
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

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders.getInstance()
                .get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }

    protected abstract void initData();

    protected abstract void initView(ViewDataBinding binding);

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected void handleIntent(@NonNull Intent intent) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }


    public ConfigureInfo getConfigureInfo() {
        return ConfigureInfo.defaultConfigure();
    }

    protected void initPullRefreshView(PtrFrameLayout refreshView) {
        final MaterialHeader header = new MaterialHeader(this);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, (int) ViewUtil.dip2px(this, 15), 0, (int) ViewUtil.dip2px(this, 10));
        header.setPtrFrameLayout(refreshView);
        refreshView.setHeaderView(header);
        refreshView.addPtrUIHandler(header);
        refreshView.setPinContent(true);
        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                LiveData<Void> pullToRefresh = mViewModel.onPullToRefresh();
                pullToRefresh.subscribe(BaseActivity.this, new LiveCallBack<Void>() {
                    @Override
                    public void onComplete(Void aVoid) {
                        frame.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable t) {
                        frame.refreshComplete();
                    }
                });
            }
        });
    }

    protected void initLoadingStateView(LoadStateView loadStateView) {
        DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.view_loading_state, loadStateView,
                        true);
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
}
