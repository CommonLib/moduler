package com.architecture.extend.baselib.util;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.base.BaseRecycleAdapter;
import com.architecture.extend.baselib.base.LoadMoreCallBack;
import com.architecture.extend.baselib.widget.LoadStateView;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class ViewUtil {

    /**
     * 描述：重置AbsListView的高度. item 的最外层布局要用
     * RelativeLayout,如果计算的不准，就为RelativeLayout指定一个高度
     *
     * @param absListView   the abs list view
     * @param lineNumber    每行几个 ListView一行一个item
     * @param verticalSpace the vertical space
     */
    public static void setAbsListViewHeight(AbsListView absListView, int lineNumber,
                                            int verticalSpace) {

        int totalHeight = getAbsListViewHeight(absListView, lineNumber, verticalSpace);
        ViewGroup.LayoutParams params = absListView.getLayoutParams();
        params.height = totalHeight;
        ((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        absListView.setLayoutParams(params);
    }

    /**
     * 描述：获取AbsListView的高度.
     *
     * @param absListView   the abs list view
     * @param lineNumber    每行几个 ListView一行一个item
     * @param verticalSpace the vertical space
     * @return the abs list view height
     */
    public static int getAbsListViewHeight(AbsListView absListView, int lineNumber,
                                           int verticalSpace) {
        int totalHeight = 0;
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        absListView.measure(w, h);
        ListAdapter mListAdapter = absListView.getAdapter();
        if (mListAdapter == null) {
            return totalHeight;
        }

        int count = mListAdapter.getCount();
        if (absListView instanceof ListView) {
            for (int i = 0; i < count; i++) {
                View listItem = mListAdapter.getView(i, null, absListView);
                listItem.measure(w, h);
                totalHeight += listItem.getMeasuredHeight();
            }
            if (count == 0) {
                totalHeight = verticalSpace;
            } else {
                totalHeight =
                        totalHeight + (((ListView) absListView).getDividerHeight() * (count - 1));
            }

        } else if (absListView instanceof GridView) {
            int remain = count % lineNumber;
            if (remain > 0) {
                remain = 1;
            }
            if (mListAdapter.getCount() == 0) {
                totalHeight = verticalSpace;
            } else {
                View listItem = mListAdapter.getView(0, null, absListView);
                listItem.measure(w, h);
                int line = count / lineNumber + remain;
                totalHeight = line * listItem.getMeasuredHeight() + (line - 1) * verticalSpace;
            }

        }
        return totalHeight;

    }

    /**
     * 测量这个view
     * 最后通过getMeasuredWidth()获取宽度和高度.
     *
     * @param view 要测量的view
     * @return 测量过的view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
    }

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

    /**
     * 获得这个View的宽度
     * 测量这个view，最后通过getMeasuredWidth()获取宽度.
     *
     * @param view 要测量的view
     * @return 测量过的view的宽度
     */
    public static int getViewWidth(View view) {
        measureView(view);
        return view.getMeasuredWidth();
    }

    /**
     * 获得这个View的高度
     * 测量这个view，最后通过getMeasuredHeight()获取高度.
     *
     * @param view 要测量的view
     * @return 测量过的view的高度
     */
    public static int getViewHeight(View view) {
        measureView(view);
        return view.getMeasuredHeight();
    }

    public static int getActionbarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * 从父亲布局中移除自己
     *
     * @param v
     */
    public static void removeSelfFromParent(View v) {
        ViewParent parent = v.getParent();
        if (parent != null) {
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(v);
            }
        }
    }


    /**
     * 描述：dip转换为px.
     *
     * @param context  the context
     * @param dipValue the dip value
     * @return px值
     */
    public static float dip2px(Context context, float dipValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, mDisplayMetrics);
    }

    /**
     * 描述：px转换为dip.
     *
     * @param context the context
     * @param pxValue the px value
     * @return dip值
     */
    public static float px2dip(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.density;
    }

    /**
     * 描述：sp转换为px.
     *
     * @param context the context
     * @param spValue the sp value
     * @return sp值
     */
    public static float sp2px(Context context, float spValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mDisplayMetrics);
    }

    /**
     * 描述：px转换为sp.
     *
     * @param context the context
     * @return sp值
     */
    public static float px2sp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.scaledDensity;
    }

    /**
     * TypedValue官方源码中的算法，任意单位转换为PX单位
     *
     * @param unit    TypedValue.COMPLEX_UNIT_DIP
     * @param value   对应单位的值
     * @param metrics 密度
     * @return px值
     */
    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    public static void showToast(@StringRes int resId) {
        Toast.makeText(BaseApplication.getInstance(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(@StringRes int resId) {
        Toast.makeText(BaseApplication.getInstance(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showToast(CharSequence text) {
        Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(CharSequence text) {
        Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_LONG).show();
    }

    public static View inflate(Context context, @LayoutRes int id) {
        return inflate(context, id, null);
    }

    public static View inflate(Context context, @LayoutRes int id, @Nullable ViewGroup root) {
        return LayoutInflater.from(context).inflate(id, root, false);
    }

    public static Intent newIntent(Context context, Class<?> cls) {
        return new Intent(context, cls);
    }

    public static void startActivity(Context context, Class<?> cls) {
        context.startActivity(newIntent(context, cls));
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
