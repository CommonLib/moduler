package com.architecture.extend.baselib.util;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.architecture.extend.baselib.base.PermissionCallBack;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;


/**
 * Created by byang059 on 9/22/17.
 */
public class PermissionAccessUtil {

    public static PermissionUtil.PermissionRequestObject requestPermission(
            AppCompatActivity activity, final String permission, final PermissionCallBack callBack) {
        return PermissionUtil.with(activity).request(permission).onAllGranted(new Func() {
            @Override
            protected void call() {
                callBack.onGranted(permission);
            }
        }).onAnyDenied(new Func() {
            @Override
            protected void call() {
                callBack.onDenied(permission);
            }
        }).ask((short)permission.hashCode());
    }

    public static PermissionUtil.PermissionRequestObject requestPermission(Fragment fragment,
                                                                           final String permission,
                                                                           final PermissionCallBack callBack) {
        return PermissionUtil.with(fragment).request(permission).onAllGranted(new Func() {
            @Override
            protected void call() {
                callBack.onGranted(permission);
            }
        }).onAnyDenied(new Func() {
            @Override
            protected void call() {
                callBack.onDenied(permission);
            }
        }).ask((short)permission.hashCode());
    }

    public static boolean hasPermission(AppCompatActivity activity, String permission) {
        return PermissionUtil.with(activity).has(permission);
    }

    public static boolean hasPermission(Fragment fragment, String permission) {
        return PermissionUtil.with(fragment).has(permission);
    }
}
