package com.architecture.extend.baselib.util;

import java.lang.reflect.ParameterizedType;

/**
 * Created by baixiaokang on 16/4/30.
 */
public class GenericUtil {
    public static <T> T instanceT(Object o, int i) throws Exception {
        return (T) getGenericsSuperType(o, i).newInstance();
    }

    public static <T> Class<T> getGenericsSuperType(Object o, int i) {
        return (Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass()))
                .getActualTypeArguments()[i];
    }
}
