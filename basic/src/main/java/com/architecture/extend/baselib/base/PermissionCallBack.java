package com.architecture.extend.baselib.base;

/**
 * Created by byang059 on 9/22/17.
 */

public interface PermissionCallBack {
    void onGranted(String permission);

    void onDenied(String permission);
}
