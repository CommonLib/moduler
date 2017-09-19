package com.architecture.extend.baselib.mvvm;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.extend.baselib.base.ShareDataViewModel;


/**
 * Created by byang059 on 6/6/17.
 */

public class BaseDialog extends DialogFragment {

    private BaseActivity mActivity;

    public static final String LAYOUT_ID = "layoutId";
    public static final String CLASS_VIEW_MODEL = "viewModel";
    private BaseViewModel mViewModel;
    private int mLayoutId;

    public static BaseDialog newInstance(Class<? extends BaseViewModel> viewModel,
                                         @LayoutRes int layoutId) {
        Bundle args = new Bundle();
        args.putSerializable(CLASS_VIEW_MODEL, viewModel);
        args.putSerializable(LAYOUT_ID, layoutId);
        BaseDialog fragment = new BaseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        Class<? extends BaseViewModel> viewModel = (Class<? extends BaseViewModel>) arguments
                .getSerializable(CLASS_VIEW_MODEL);
        mLayoutId = arguments.getInt(LAYOUT_ID);
        initArguments(arguments);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        if(mLayoutId > 0){
            ViewDataBinding binding = DataBindingUtil
                    .inflate(inflater, mLayoutId, container, false);
//            binding.setVariable(BR._all, mViewModel);
            return binding.getRoot();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void initArguments(Bundle arguments) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = (ShareDataViewModel) ViewModelProviders
                .getInstance().get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }
}
