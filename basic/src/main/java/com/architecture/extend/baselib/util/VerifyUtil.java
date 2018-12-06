package com.architecture.extend.baselib.util;

/**
 * Created by byang059 on 2018/12/6.
 */

public class VerifyUtil {

    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }
}
