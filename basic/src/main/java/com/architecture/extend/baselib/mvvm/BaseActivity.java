package com.architecture.extend.baselib.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.base.ShareDataViewModel;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.widget.LoadStateFrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by byang059 on 12/19/16.
 */

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity {

    private VM mViewModel;
    private boolean mIsForeground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<VM> viewModelClazz = GenericUtil.getGenericsSuperType(this, 0);
        mViewModel = ViewModelProviders.getInstance().get(viewModelClazz);
        mViewModel.onViewCreate();
        setContentView(getLayoutId());
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
        initData();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mIsForeground = false;
        mViewModel.onViewStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsForeground = true;
        mViewModel.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsForeground = false;
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
        mViewModel.onViewStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onViewDestroy();
    }

    public VM getViewModel() {
        return mViewModel;
    }

    public boolean isForeground() {
        return mIsForeground;
    }

    protected View inflate(@LayoutRes int id) {
        return inflate(id, null);
    }

    protected View inflate(@LayoutRes int id, @Nullable ViewGroup root) {
        return LayoutInflater.from(this).inflate(id, root);
    }

    protected Intent newIntent(Class<?> cls) {
        return new Intent(this, cls);
    }

    protected void startActivity(Class<?> cls) {
        startActivity(newIntent(cls));
    }

    protected void showToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    protected void showToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    protected DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    protected int getWidthPixels() {
        return getDisplayMetrics().widthPixels;
    }

    protected int getHeightPixels() {
        return getDisplayMetrics().heightPixels;
    }

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders
                .getInstance().get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }

    /**
     * @param targetView 用户要添加加载状态的view
     * @return 添加加载状态
     */
    public LoadStateFrameLayout addLoadingStateView(View targetView) {
        LoadStateFrameLayout loadStateView = new LoadStateFrameLayout(this);
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
            loadStateView.addSuccessView(targetView);
            parent.addView(loadStateView, index, targetParams);
        } else {
            loadStateView.setLayoutParams(targetParams);
            loadStateView.addSuccessView(targetView);
        }
        return loadStateView;
    }

    /**
     * @param
     * @return 给一个View添加下拉刷新
     */
    public PtrFrameLayout addPullToRefreshView(View targetView) {
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
            ptr = (PtrFrameLayout) LayoutInflater.from(this)
                    .inflate(R.layout.fragment_base, parent, false);
            FrameLayout content = (FrameLayout) ptr.getContentView();
            content.addView(targetView);
            parent.addView(ptr, index, targetParams);
        } else {
            ptr = (PtrFrameLayout) LayoutInflater.from(this)
                    .inflate(R.layout.fragment_base, null, false);
            FrameLayout content = (FrameLayout) ptr.getContentView();
            ptr.setLayoutParams(targetParams);
            content.addView(targetView);

        }
        return ptr;
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract @LayoutRes int getLayoutId();

    protected void handleIntent(@NonNull Intent intent) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }
}
