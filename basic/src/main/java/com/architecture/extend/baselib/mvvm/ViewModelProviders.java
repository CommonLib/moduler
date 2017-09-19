package com.architecture.extend.baselib.mvvm;

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

    public <T extends BaseViewModel> T get(Class<T> clazz) {
        T viewModel = (T) mViewModels.get(clazz.getName());
        if (viewModel == null) {
            try {
                viewModel = clazz.newInstance();
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
