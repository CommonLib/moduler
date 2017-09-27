package com.architecture.extend.baselib.mvvm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
 * Created by appledev116 on 3/10/16.
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment implements ViewAble {

    private VM mViewModel;
    private boolean mIsForeground;
    private boolean mIsDestroyed;
    private BaseActivity mActivity;
    private ViewForegroundSwitchListener mSwitchListener;
    private ArrayList<PermissionUtil.PermissionRequestObject> mPermissionRequests;
    private ConfigureInfo mConfigureInfo;
    private PtrFrameLayout mPullToRefreshView;
    private LoadStateView mLoadStateView;

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
        setForegroundSwitchCallBack(mViewModel);
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mConfigureInfo = getConfigureInfo();
        View content = null;
        int layoutId = getLayoutId();
        if (mConfigureInfo.isAsyncInflate() && layoutId > 0) {
            FrameLayout frameLayout = new FrameLayout(mActivity);
            frameLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            asyncInflateLayout(frameLayout, inflater, layoutId);
            content = frameLayout;
            //TODO TEST
        } else {
            content = inflater.inflate(layoutId, container, false);
            content = packageContentView(mConfigureInfo, content);
            init();
        }
        return content;
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsForeground = true;
        mIsDestroyed = false;
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
        mIsDestroyed = true;
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

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected void handleArguments(@NonNull Bundle arguments) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }

    /**
     * for android verision above 23 to apply permission
     *
     * @param permission
     * @param callBack
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void usePermission(String permission, PermissionCallBack callBack) {
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

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders.getInstance()
                .get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }

    @Override
    public void setForegroundSwitchCallBack(ViewForegroundSwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

    private void asyncInflateLayout(final ViewGroup parent, LayoutInflater inflater,
                                    @LayoutRes int layoutId) {
        LiveData<View> inflate = getViewModel().asyncInflate(inflater, parent, layoutId);
        inflate.subscribe(this, new UiCallBack<View>() {
            @Override
            public void onComplete(View view) {
                View packageView = packageContentView(mConfigureInfo, view);
                parent.addView(packageView);
                init();
            }
        });
    }

    public void init() {
        initView();
        initData();
    }

    public ConfigureInfo getConfigureInfo() {
        return ConfigureInfo.defaultConfigure();
    }

    public View packageContentView(ConfigureInfo configureInfo, View view) {
        View contentView = view;

        if (configureInfo.isLoadingState()) {
            mLoadStateView = ViewUtil.addLoadingStateView(contentView);
            initLoadingStateView(mLoadStateView);
            contentView = mLoadStateView;
        }

        if (configureInfo.isPullToRefresh()) {
            mPullToRefreshView = ViewUtil.addPullToRefreshView(contentView);
            initPullRefreshView(mPullToRefreshView);
            contentView = mPullToRefreshView;
        }

        Boolean enableToolbar = configureInfo.isEnableToolbar();
        Boolean actEnableToolbar = mActivity.getConfigureInfo().isEnableToolbar();
        if (enableToolbar != null && enableToolbar && actEnableToolbar != null
                && actEnableToolbar) {
            ViewUtil.showSupportActionBar(mActivity);
        } else if (enableToolbar == null){
            //sub fragment not specific show or not, follow act
        }else{
            ViewUtil.hideSupportActionBar(mActivity);
        }
        return contentView;
    }

    protected void initPullRefreshView(PtrFrameLayout refreshView) {
        final MaterialHeader header = new MaterialHeader(mActivity);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, (int) ViewUtil.dip2px(mActivity, 15), 0,
                (int) ViewUtil.dip2px(mActivity, 10));
        header.setPtrFrameLayout(refreshView);
        refreshView.setHeaderView(header);
        refreshView.addPtrUIHandler(header);
        refreshView.setPinContent(true);
        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                LiveData<Void> pullToRefresh = mViewModel.onPullToRefresh();
                pullToRefresh.subscribe(BaseFragment.this, new UiCallBack<Void>() {
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
                .inflate(LayoutInflater.from(mActivity), R.layout.view_loading_state, loadStateView,
                        true);
    }

    public PtrFrameLayout getPullToRefreshView() {
        return mPullToRefreshView;
    }

    public LoadStateView getLoadStateView() {
        return mLoadStateView;
    }
}