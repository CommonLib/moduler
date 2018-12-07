package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.architecture.extend.baselib.widget.ChildScrollFrameLayout;

/**
 * Created by byang059 on 9/20/17.
 */

public interface ViewLayer {
    boolean isForeground();

    void setForegroundSwitchCallBack(ViewForegroundSwitchListener listener);

    boolean isDestroyed();

    BaseViewModel getViewModel();

    Lifecycle getLifecycle();

    LifecycleOwner getLifecycleOwner();

    View packageContentView(ConfigureInfo configureInfo, View view);

    default View initViewFromConfigureInfo(ConfigureInfo configureInfo, LayoutInflater inflater,
                                           ViewGroup container, Activity activity, int layoutId) {
        if (layoutId <= 0) {
            return null;
        }
        if (configureInfo.isAsyncInflate()) {
            if (this instanceof Activity) {
                LiveData<View> inflate = getViewModel().asyncInflate(layoutId, inflater, container);
                inflate.observe(getLifecycleOwner(), view -> {
                    attachContentView(container, packageContentView(configureInfo, view));
                    init(DataBindingUtil.bind(view));
                });
                return null;
            } else {
                FrameLayout frameLayout = new ChildScrollFrameLayout(activity);
                frameLayout.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                LiveData<View> inflate = getViewModel()
                        .asyncInflate(layoutId, inflater, frameLayout);
                inflate.observe(getLifecycleOwner(), view -> {
                    attachContentView(frameLayout, packageContentView(configureInfo, view));
                    init(DataBindingUtil.bind(view));
                });
                return frameLayout;
            }

        }
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        View content = packageContentView(configureInfo, binding.getRoot());
        attachContentView(container, content);
        init(binding);
        return content;
    }

    default void init(ViewDataBinding dataBinding){
        initView(dataBinding);
        initData();
    }

    void initData();

    void initView(ViewDataBinding dataBinding);

    void attachContentView(ViewGroup viewGroup, View content);
}
