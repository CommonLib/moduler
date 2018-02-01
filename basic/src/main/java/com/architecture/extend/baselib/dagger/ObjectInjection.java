package com.architecture.extend.baselib.dagger;

import com.architecture.extend.baselib.BaseApplication;

import dagger.android.AndroidInjector;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by byang059 on 1/31/18.
 */

public class ObjectInjection {

    public static void inject(ApplicationAble object) {
        checkNotNull(object, "object");
        BaseApplication application = object.getApplication();
        if (!(application instanceof HasObjectInjector)) {
            throw new RuntimeException(String.format("%s does not implement %s",
                    application.getClass().getCanonicalName(),
                    HasObjectInjector.class.getCanonicalName()));
        }

        AndroidInjector<Object> viewModelInjector =
                ((HasObjectInjector) application).objectInjector();
        checkNotNull(viewModelInjector, "%s.objectInjector() returned null", application.getClass());
        viewModelInjector.inject(object);
    }
}
