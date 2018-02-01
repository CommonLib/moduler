package com.architecture.extend.baselib.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import dagger.MapKey;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by byang059 on 2/1/18.
 */
@MapKey
@Documented
@Retention(RUNTIME)
public @interface ObjectKey {
    Class<? extends Object> value();
}
