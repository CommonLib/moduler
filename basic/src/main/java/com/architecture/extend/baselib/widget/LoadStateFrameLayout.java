package com.architecture.extend.baselib.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.databinding.ViewLoadingStateBinding;


/**
 * Created by l on 2016/3/9.
 * 自定义View，根据不同的状态显示
 */
public class LoadStateFrameLayout extends FrameLayout {

    public static final int STATE_USER = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_EMPTY = 3;

    private int state = STATE_USER;
    private View mSuccessView;
    private ViewLoadingStateBinding mBinding;

    public LoadStateFrameLayout(Context context) {
        this(context, null);
    }

    public LoadStateFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadStateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.view_loading_state, this, true);
    }

    public void addSuccessView(View successView) {
        if (successView != null && successView.getParent() == null) {
            addView(successView);
            mSuccessView = successView;
        } else {
            throw new IllegalArgumentException("successView is null or have a parent view");
        }
    }


    /**
     * 根据不同的状态值，更新当前的View
     *
     * @param currentState
     */
    public void updateState(int currentState) {
        switch (currentState) {
            case STATE_USER:
                mSuccessView.setVisibility(View.VISIBLE);
                mBinding.viewStateEmpty.setVisibility(View.GONE);
                mBinding.viewStateError.setVisibility(View.GONE);
                mBinding.viewStateLoading.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                mSuccessView.setVisibility(View.INVISIBLE);
                mBinding.viewStateEmpty.setVisibility(View.GONE);
                mBinding.viewStateError.setVisibility(View.VISIBLE);
                mBinding.viewStateLoading.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                mSuccessView.setVisibility(View.INVISIBLE);
                mBinding.viewStateEmpty.setVisibility(View.GONE);
                mBinding.viewStateError.setVisibility(View.GONE);
                mBinding.viewStateLoading.setVisibility(View.VISIBLE);
                break;
            case STATE_EMPTY:
                mSuccessView.setVisibility(View.INVISIBLE);
                mBinding.viewStateEmpty.setVisibility(View.VISIBLE);
                mBinding.viewStateError.setVisibility(View.GONE);
                mBinding.viewStateLoading.setVisibility(View.GONE);
                break;
            default:
                throw new IllegalArgumentException("can'result not recognize view state");
        }

        state = currentState;
    }

    public void setEmptyText(String text, float size, int color){
        mBinding.viewStateEmpty.setText(text);
        mBinding.viewStateEmpty.setTextSize(size);
        mBinding.viewStateEmpty.setTextColor(color);
    }

    public void setEmptyBackground(int color){
        mBinding.viewStateEmpty.setBackgroundColor(color);
    }

    public int getState(){
        return state;
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
}
