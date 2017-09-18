package com.architecture.extend.baselib.base;

import java.util.HashMap;

/**
 * Created by byang059 on 9/18/17.
 */

public class ViewModelProviders {
    private static ViewModelProviders sInstance = new ViewModelProviders();
    private HashMap<String, ViewModel> mViewModels = null;

    private ViewModelProviders() {
        mViewModels = new HashMap<>();
    }

    public static ViewModelProviders getInstance() {
        return sInstance;
    }

    public ViewModel get(Class<?> clazz) {
        ViewModel viewModel = mViewModels.get(clazz.getName());
        if (viewModel == null) {
            try {
                viewModel = (ViewModel) clazz.newInstance();
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
