package com.architecture.extend.baselib.storage.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * used to ignore some field when be serialized to json
 * Created by bkang016 on 6/14/17.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {


}
