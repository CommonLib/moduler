package com.architecture.extend.baselib.util;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.base.BaseRecycleAdapter;
import com.architecture.extend.baselib.base.LoadMoreCallBack;
import com.architecture.extend.baselib.widget.LoadStateView;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class ViewUtil {

    /**
     * @param
     * @return 给一个View添加下拉刷新
     */
    public static PtrFrameLayout addPullToRefreshView(View targetView) {
        ViewGroup parent = (ViewGroup) targetView.getParent();
        ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PtrFrameLayout ptr = null;

        if (targetParams == null) {
            targetParams = params;
        }

        targetView.setLayoutParams(params);
        if (parent != null) {
            int index = parent.indexOfChild(targetView);
            parent.removeView(targetView);
            ptr = (PtrFrameLayout) LayoutInflater.from(targetView.getContext())
                    .inflate(R.layout.view_pull_refresh, parent, false);
            FrameLayout content = (FrameLayout) ptr.getContentView();
            content.addView(targetView);
            parent.addView(ptr, index, targetParams);
        } else {
            ptr = (PtrFrameLayout) LayoutInflater.from(targetView.getContext())
                    .inflate(R.layout.view_pull_refresh, null, false);
            FrameLayout content = (FrameLayout) ptr.getContentView();
            ptr.setLayoutParams(targetParams);
            content.addView(targetView);
        }
        return ptr;
    }

    /**
     * @param targetView 用户要添加加载状态的view
     * @return 添加加载状态
     */
    public static LoadStateView addLoadingStateView(View targetView) {
        LoadStateView loadStateView = new LoadStateView(targetView.getContext());
        ViewGroup parent = (ViewGroup) targetView.getParent();
        ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (targetParams == null) {
            targetParams = params;
        }

        targetView.setLayoutParams(params);
        if (parent != null) {
            int index = parent.indexOfChild(targetView);
            parent.removeView(targetView);
            loadStateView.setSuccessView(targetView);
            parent.addView(loadStateView, index, targetParams);
        } else {
            loadStateView.setLayoutParams(targetParams);
            loadStateView.setSuccessView(targetView);
        }
        return loadStateView;
    }

    /**
     * @param
     * @return 给一个View添加下拉刷新
     */
    public static LinearLayout addToolBarView(Context context, @LayoutRes int toolBarLayoutId,
                                              View contentView) {
        LinearLayout parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(toolBarLayoutId, parent, true);

        ViewGroup.LayoutParams contentViewLayoutParams = contentView.getLayoutParams();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        if (contentViewLayoutParams != null) {
            parent.setLayoutParams(contentViewLayoutParams);
        }
        parent.addView(contentView);
        return parent;
    }

    public static View inflate(Context context, @LayoutRes int id) {
        return inflate(context, id, null);
    }

    public static View inflate(Context context, @LayoutRes int id, @Nullable ViewGroup root) {
        return LayoutInflater.from(context).inflate(id, root, false);
    }

    public static void showSupportActionBar(AppCompatActivity activity) {
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null && !supportActionBar.isShowing()) {
            supportActionBar.show();
        }
    }

    public static void hideSupportActionBar(AppCompatActivity activity) {
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null && supportActionBar.isShowing()) {
            supportActionBar.hide();
        }
    }

    /**
     * add load more to recycleView
     * @param recycleView
     * @param adapter
     * @param layoutManager
     * @param loadMoreBinding
     * @param callBack
     */
    public static void setUpRecycleViewLoadMore(RecyclerView recycleView,
                                                final BaseRecycleAdapter adapter,
                                                final LinearLayoutManager layoutManager,
                                                ViewDataBinding loadMoreBinding,int type,
                                                final LoadMoreCallBack callBack) {

        final View loadMoreView = loadMoreBinding.getRoot();
        adapter.addFooter(type, loadMoreBinding);
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (adapter.isHeaderType(position) || adapter.isFooterType(position))
                            || adapter.isUserItemType(position) ? manager.getSpanCount() : 1;
                }
            });
        }

        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastBottomPosition = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (loadMoreView.getVisibility() == View.VISIBLE) {
                    int bottomPosition = layoutManager.findLastVisibleItemPosition();
                    if (bottomPosition == adapter.getItemCount() - 1
                            && bottomPosition != lastBottomPosition) {
                        callBack.onLoadMore();
                    }
                    lastBottomPosition = bottomPosition;
                }
            }
        });
    }
}
