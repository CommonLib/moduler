package com.architecture.extend.baselib.base;

import java.util.HashMap;

/**
 * Created by byang059 on 9/18/17.
 */

public class ViewModelProviders {
    private static ViewModelProviders sInstance = new ViewModelProviders();
    private HashMap<String, BaseViewModel> mViewModels = null;

    private ViewModelProviders() {
        mViewModels = new HashMap<>();
    }

    public static ViewModelProviders getInstance() {
        return sInstance;
    }

    public BaseViewModel get(Class<?> clazz) {
        BaseViewModel viewModel = mViewModels.get(clazz.getName());
        if (viewModel == null) {
            try {
                viewModel = (BaseViewModel) clazz.newInstance();
                mViewModels.put(clazz.getName(), viewModel);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return viewModel;
    }
}
