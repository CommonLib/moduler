package com.architecture.extend.baselib.mvvm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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
import android.widget.FrameLayout;

import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.base.PermissionCallBack;
import com.architecture.extend.baselib.dagger.InjectionUtil;
import com.architecture.extend.baselib.dagger.Injector;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.ViewUtil;
import com.architecture.extend.baselib.widget.ChildScrollFrameLayout;
import com.architecture.extend.baselib.widget.LoadStateView;
import com.blankj.utilcode.util.SizeUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by burtYang on 10/09/17.
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment
        implements Viewable, MessageQueue.IdleHandler, RequestPermissionAble {

    private VM mViewModel;
    private boolean mIsForeground;
    private boolean mIsDestroyed;
    private BaseActivity mActivity;
    private ViewForegroundSwitchListener mSwitchListener;
    private ConfigureInfo mConfigureInfo;
    private PtrFrameLayout mPullToRefreshView;
    private LoadStateView mLoadStateView;
    private AndroidInjector<Fragment> mInjector;
    @Inject
    MessageQueue mMessageQueue;

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
        mConfigureInfo = getConfigureInfo();
        View content = null;
        int layoutId = getLayoutId();
        if (mConfigureInfo.isAsyncInflate() && layoutId > 0) {
            FrameLayout frameLayout = new ChildScrollFrameLayout(mActivity);
            frameLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            asyncInflateLayout(frameLayout, inflater, layoutId);
            content = frameLayout;
        } else {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
            content = packageContentView(mConfigureInfo, binding.getRoot());
            init(binding);
        }
        return content;
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

    protected abstract void initData();

    protected abstract void initView(ViewDataBinding dataBinding);

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

    private void asyncInflateLayout(final ViewGroup parent, LayoutInflater inflater,
                                    @LayoutRes int layoutId) {
        LiveData<View> inflate = getViewModel().asyncInflate(layoutId, inflater, parent);
        inflate.observe(this, view -> {
            View packageView = packageContentView(mConfigureInfo, view);
            parent.addView(packageView);
            init(DataBindingUtil.bind(view));
        });
    }

    private void init(ViewDataBinding dataBinding) {
        initView(dataBinding);
        initData();
    }

    protected ConfigureInfo getConfigureInfo() {
        return ConfigureInfo.defaultConfigure();
    }

    private View packageContentView(ConfigureInfo configureInfo, View view) {
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
        } else if (enableToolbar == null) {
            //sub fragment not specific show or not, follow act
        } else {
            ViewUtil.hideSupportActionBar(mActivity);
        }
        return contentView;
    }

    protected void initPullRefreshView(PtrFrameLayout refreshView) {
        final MaterialHeader header = new MaterialHeader(mActivity);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, SizeUtils.dp2px(15), 0, SizeUtils.dp2px(15));
        header.setPtrFrameLayout(refreshView);
        refreshView.setHeaderView(header);
        refreshView.addPtrUIHandler(header);
        refreshView.setPinContent(true);
        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefreshBegin(frame);
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

    public <T> T instanceViewModel(AndroidInjector<Fragment> injector) {
        Class viewModelClazz = GenericUtil.getGenericsSuperType(this, 0);
        Object viewModel = ViewModelProviders.of(this).get(viewModelClazz);
        if (injector instanceof Injector) {
            ((Injector) injector).injectViewModel(viewModel);
        }
        return (T) viewModel;
    }

    public void startPage(Bundle bundle, String path) {
        mViewModel.startPage(bundle, path);
    }

    public void startPageForResult(Bundle bundle, String path, int requestCode) {
        mViewModel.startPageForResult(bundle, path, mActivity, requestCode);
    }

    protected void onRefreshBegin(PtrFrameLayout frame) {
    }

    protected void initLoadingStateView(LoadStateView loadStateView) {
        DataBindingUtil
                .inflate(LayoutInflater.from(mActivity), R.layout.view_loading_state, loadStateView,
                        true);
    }

    protected PtrFrameLayout getPullToRefreshView() {
        return mPullToRefreshView;
    }

    protected LoadStateView getLoadStateView() {
        return mLoadStateView;
    }

    protected BaseActivity getBindActivity() {
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
}