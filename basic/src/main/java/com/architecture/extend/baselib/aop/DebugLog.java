package com.architecture.extend.baselib.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by byang059 on 2018/6/20.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface DebugLog {

}
