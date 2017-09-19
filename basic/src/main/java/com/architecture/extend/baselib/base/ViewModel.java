package com.architecture.extend.baselib.base;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by byang059 on 9/18/17.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModel {

    Class<?> viewModel() default Object.class;
}
