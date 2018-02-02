package com.architecture.extend.baselib.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by byang059 on 2/2/18.
 */
@Scope
@Retention(RUNTIME)
@Documented
public @interface ApplicationScope {}
