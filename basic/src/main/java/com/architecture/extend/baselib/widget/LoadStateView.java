package com.architecture.extend.baselib.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.architecture.extend.baselib.R;


/**
 * Created by l on 2016/3/9.
 * 自定义View，根据不同的状态显示
 */
public class LoadStateView extends FrameLayout {

    private static final int STATE_SUCCESS = 0;
    private static final int STATE_ERROR = 1;
    private static final int STATE_LOADING = 2;
    private static final int STATE_EMPTY = 3;

    private int mState = STATE_SUCCESS;
    private View mSuccessView;
    private View mEmptyView;
    private View mLoadingView;
    private View mErrorView;

    public LoadStateView(Context context) {
        super(context);
    }

    public LoadStateView setSuccessView(@NonNull View successView) {
        successView.setVisibility(View.VISIBLE);
        addView(successView);
        mSuccessView = successView;
        return this;
    }

    private void initOtherStateView() {
        mEmptyView = findViewById(R.id.view_state_empty);
        mLoadingView = findViewById(R.id.view_state_loading);
        mErrorView = findViewById(R.id.view_state_error);
        updateState(STATE_SUCCESS);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        initOtherStateView();
    }

    /**
     * 根据不同的状态值，更新当前的View
     *
     * @param currentState
     */
    private void updateState(int currentState) {
        switch (currentState) {
            case STATE_SUCCESS:
                showView(mSuccessView);
                goneView(mEmptyView);
                goneView(mErrorView);
                goneView(mLoadingView);
                break;
            case STATE_ERROR:
                invisibleView(mSuccessView);
                goneView(mEmptyView);
                showView(mErrorView);
                goneView(mLoadingView);
                break;
            case STATE_LOADING:
                invisibleView(mSuccessView);
                goneView(mEmptyView);
                goneView(mErrorView);
                showView(mLoadingView);
                break;
            case STATE_EMPTY:
                invisibleView(mSuccessView);
                showView(mEmptyView);
                goneView(mErrorView);
                goneView(mLoadingView);
                break;
            default:
                break;
        }
        mState = currentState;
    }

    public void showSuccess() {
        updateState(STATE_SUCCESS);
    }

    public void showEmpty() {
        updateState(STATE_EMPTY);
    }

    public void showError() {
        updateState(STATE_ERROR);
    }

    public void showLoading() {
        updateState(STATE_LOADING);
    }

    public int getState() {
        return mState;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (mSuccessView != null) {
            return mSuccessView.canScrollHorizontally(direction);
        }
        return super.canScrollHorizontally(direction);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (mSuccessView != null) {
            return mSuccessView.canScrollVertically(direction);
        }
        return super.canScrollVertically(direction);
    }

    private void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void goneView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    private void invisibleView(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
