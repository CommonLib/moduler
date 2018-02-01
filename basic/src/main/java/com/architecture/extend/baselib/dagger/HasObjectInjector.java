package com.architecture.extend.baselib.dagger;

import dagger.android.AndroidInjector;

/**
 * Created by byang059 on 1/31/18.
 */

public interface HasObjectInjector {
    AndroidInjector<Object> objectInjector();
}
