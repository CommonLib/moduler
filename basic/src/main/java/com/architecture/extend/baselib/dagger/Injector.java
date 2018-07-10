package com.architecture.extend.baselib.dagger;

import dagger.android.AndroidInjector;

/**
 * Created by byang059 on 2018/7/5.
 */

public interface Injector<T, V> extends AndroidInjector<T> {
    void injectViewModel(V viewModel);
}
