package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.util.ViewUtil;
import com.architecture.extend.baselib.widget.ChildScrollFrameLayout;
import com.architecture.extend.baselib.widget.LoadStateView;
import com.blankj.utilcode.util.SizeUtils;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import lombok.Getter;

/**
 * Created by byang059 on 2018/12/7.
 */

@Getter
public class ViewDelegate {

    private final ViewLayer context;
    private LoadStateView loadStateView;
    private PtrFrameLayout pullToRefreshView;
    private Toolbar toolbar;
    private boolean isToolbarAdd;

    public ViewDelegate(ViewLayer context) {
        this.context = context;
    }

    public View initViewFromConfigureInfo(ConfigureInfo configureInfo, LayoutInflater inflater,
                                          ViewGroup container, Activity activity, int layoutId) {
        if (layoutId <= 0) {
            return null;
        }
        if (configureInfo.isAsyncInflate()) {
            return getAsyncInflateView(configureInfo, inflater, container, activity, layoutId);
        }
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        return onViewInflated(configureInfo, container, binding);
    }

    private View getAsyncInflateView(ConfigureInfo configureInfo, LayoutInflater inflater,
                                     ViewGroup container, Activity activity, int layoutId) {
        if (context instanceof Activity) {
            asyncInflate(configureInfo, inflater, container, layoutId);
            return container;
        }
        FrameLayout frameLayout = new ChildScrollFrameLayout(activity);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        asyncInflate(configureInfo, inflater, frameLayout, layoutId);
        return frameLayout;
    }

    private void asyncInflate(ConfigureInfo configureInfo, LayoutInflater inflater,
                              ViewGroup container, int layoutId) {
        LiveData<View> inflate = context.getViewModel().asyncInflate(layoutId, inflater, container);
        inflate.observe(context.getLifecycleOwner(),
                view -> onViewInflated(configureInfo, container, DataBindingUtil.bind(view)));
    }

    private View onViewInflated(ConfigureInfo configureInfo, ViewGroup container,
                                ViewDataBinding binding) {
        View content = packageContentView(configureInfo, binding.getRoot());
        context.attachContentView(container, content);
        init(binding);
        return content;
    }

    private void init(ViewDataBinding dataBinding) {
        context.initView(dataBinding);
        context.initData();
    }

    private View packageContentView(ConfigureInfo configureInfo, View view) {
        View contentView = view;
        if (configureInfo.isLoadingState()) {
            loadStateView = ViewUtil.addLoadingStateView(contentView);
            initLoadingStateView(loadStateView);
            contentView = loadStateView;
        }

        if (configureInfo.isPullToRefresh()) {
            pullToRefreshView = ViewUtil.addPullToRefreshView(contentView);
            initPullRefreshView(pullToRefreshView);
            contentView = pullToRefreshView;
        }

        BaseActivity activity = context.getBindActivity();
        ConfigureInfo actConfigure = activity.getConfigureInfo();
        boolean actToolbarEnable = actConfigure.isToolbar();
        boolean toolbar = configureInfo.isToolbar();

        if (actToolbarEnable || toolbar) {
            if (actToolbarEnable && !activity.getViewDelegate().isToolbarAdd) {
                contentView = ViewUtil
                        .addToolBarView(activity, R.layout.view_tool_bar, contentView);
                this.toolbar = contentView.findViewById(R.id.common_tl_toolbar);
                initToolBar(this.toolbar);
                isToolbarAdd = true;
            }
            updateToolBarStatue(configureInfo, activity);
        }
        return contentView;
    }

    private void updateToolBarStatue(ConfigureInfo configureInfo, BaseActivity activity) {
        Boolean toolbarShow = configureInfo.getIsToolbarShow();
        if (toolbarShow != null) {
            if (toolbarShow) {
                ViewUtil.showSupportActionBar(activity);
            } else {
                ViewUtil.hideSupportActionBar(activity);
            }
        }
    }

    private void initLoadingStateView(LoadStateView loadStateView) {
        DataBindingUtil.inflate(LayoutInflater.from(context.getBindActivity()),
                R.layout.view_loading_state, loadStateView, true);
    }

    private void initPullRefreshView(PtrFrameLayout refreshView) {
        final MaterialHeader header = new MaterialHeader(context.getBindActivity());
        int[] colors = context.getBindActivity().getResources().getIntArray(R.array.google_colors);
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
                context.getViewModel().onPullToRefresh();
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

    private void initToolBar(Toolbar toolbar) {
        context.getBindActivity().setSupportActionBar(toolbar);
        ActionBar actionBar = context.getBindActivity().getSupportActionBar();
        if (actionBar != null) {
            //enable back button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
