package com.architecture.extend.baselib.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding mBinding;
    private Context mContext;
    private int mLayoutId;
    private int mPosition;

    private ViewHolder(Context context, ViewDataBinding binding) {
        super(binding.getRoot());
        mContext = context;
        mBinding = binding;
        mBinding.getRoot().setTag(this);
    }

    public static ViewHolder get(Context context, ViewGroup parent, int layoutId) {
        ViewDataBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), layoutId, parent, false);
        ViewHolder holder = new ViewHolder(context, binding);
        holder.mLayoutId = layoutId;
        return holder;
    }

    public static ViewHolder get(Context context, ViewDataBinding binding) {
        return new ViewHolder(context, binding);
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public Context getContext() {
        return mContext;
    }

    public void updatePosition(int position) {
        mPosition = position;
    }

}
