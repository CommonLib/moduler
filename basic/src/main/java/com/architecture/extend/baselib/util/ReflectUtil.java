package com.architecture.extend.baselib.util;

/**
 * Created by byang059 on 9/18/17.
 */

public class ReflectUtil {
    public static Object newInstance(Class<?> clazz) {
        Object obj = null;
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
