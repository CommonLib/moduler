package com.architecture.extend.baselib.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by byang059 on 9/18/17.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

    int layout() default -1;

    Class<?> viewModel() default Object.class;

    Class<?> view() default Object.class;

    Class<?> model() default Object.class;
}
