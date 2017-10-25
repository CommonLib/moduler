package com.architecture.extend.baselib.mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.base.SharedViewModel;
import com.architecture.extend.baselib.util.AppUtil;
import com.architecture.extend.baselib.util.ViewUtil;


/**
 * Created by byang059 on 6/6/17.
 */

public class BaseDialog extends DialogFragment {

    private static final String LISTENER = "listener";
    private static final String WIDTH = "mWidth";
    private static final String HEIGHT = "mHeight";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String CANCEL = "out_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";

    private int mWidth;//宽度
    private int mHeight;//高度
    private float mDimAmount = 0.5f;//灰度深浅
    private boolean mShowBottom;//是否底部显示
    private boolean mOutCancel = true;//是否点击外部取消
    @StyleRes
    private int mAnimStyle;
    @LayoutRes
    protected int mLayoutId;
    private ViewCreateCallBack mViewCreateCallBack;

    public static BaseDialog newInstance() {
        return new BaseDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialog);
        if (savedInstanceState != null) {
            mWidth = savedInstanceState.getInt(WIDTH);
            mHeight = savedInstanceState.getInt(HEIGHT);
            mDimAmount = savedInstanceState.getFloat(DIM);
            mShowBottom = savedInstanceState.getBoolean(BOTTOM);
            mOutCancel = savedInstanceState.getBoolean(CANCEL);
            mAnimStyle = savedInstanceState.getInt(ANIM);
            mLayoutId = savedInstanceState.getInt(LAYOUT);
            mViewCreateCallBack = savedInstanceState.getParcelable(LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, mLayoutId, container, false);
        initView(binding, this);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(WIDTH, mWidth);
        outState.putInt(HEIGHT, mHeight);
        outState.putFloat(DIM, mDimAmount);
        outState.putBoolean(BOTTOM, mShowBottom);
        outState.putBoolean(CANCEL, mOutCancel);
        outState.putInt(ANIM, mAnimStyle);
        outState.putInt(LAYOUT, mLayoutId);
        outState.putParcelable(LISTENER, mViewCreateCallBack);
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = mDimAmount;
            //是否在底部显示
            if (mShowBottom) {
                lp.gravity = Gravity.BOTTOM;
                if (mAnimStyle == 0) {
                    mAnimStyle = R.style.DialogDefaultAnimation;
                }
            }

            //设置dialog宽度
            if (mWidth == 0) {
                lp.width = AppUtil.getScreenWidth(BaseApplication.getInstance());
            } else if (mWidth == -1) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.width = (int) ViewUtil.dip2px(BaseApplication.getInstance(), mWidth);
            }

            //设置dialog高度
            if (mHeight == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.height = (int) ViewUtil.dip2px(BaseApplication.getInstance(), mHeight);
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(mAnimStyle);
            window.setAttributes(lp);
        }
        setCancelable(mOutCancel);
    }

    public BaseDialog setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    public BaseDialog setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public BaseDialog setDimAmount(float dimAmount) {
        this.mDimAmount = dimAmount;
        return this;
    }

    public BaseDialog setShowBottom(boolean showBottom) {
        this.mShowBottom = showBottom;
        return this;
    }

    public BaseDialog setOutCancel(boolean outCancel) {
        this.mOutCancel = outCancel;
        return this;
    }

    public BaseDialog setAnimStyle(@StyleRes int animStyle) {
        this.mAnimStyle = animStyle;
        return this;
    }

    public BaseDialog show(FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        if (this.isAdded()) {
            ft.remove(this).commit();
        }
        ft.add(this, String.valueOf(System.currentTimeMillis()));
        ft.commitAllowingStateLoss();
        return this;
    }

    private void initView(ViewDataBinding dataBinding, BaseDialog dialog) {
        if (mViewCreateCallBack != null) {
            mViewCreateCallBack.initView(dataBinding, dialog);
        }
    }

    public BaseDialog setLayoutId(@LayoutRes int layoutId, ViewCreateCallBack viewCreateCallBack) {
        mLayoutId = layoutId;
        this.mViewCreateCallBack = viewCreateCallBack;
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewCreateCallBack = null;
    }

    protected Object getSharedData(String key) {
        SharedViewModel sharedViewModel = ViewModelProviders.getInstance()
                .get(SharedViewModel.class);
        return sharedViewModel.get(key);
    }
}
