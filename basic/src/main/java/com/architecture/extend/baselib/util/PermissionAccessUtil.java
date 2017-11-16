package com.architecture.extend.baselib.util;

import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.architecture.extend.baselib.base.PermissionCallBack;
import com.github.kayvannj.permission_utils.Func2;
import com.github.kayvannj.permission_utils.PermissionUtil;


/**
 * Created by byang059 on 9/22/17.
 */
public class PermissionAccessUtil {

    public static PermissionUtil.PermissionRequestObject requestPermission(
            AppCompatActivity activity, final String[] permissions,
            final PermissionCallBack callBack) {

        return PermissionUtil.with(activity).request(permissions).onResult(new Func2() {
            @Override
            protected void call(int requestCode, String[] permissions, int[] grantResults) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        callBack.onGranted(permissions[i]);
                    } else {
                        callBack.onDenied(permissions[i]);
                    }
                }
            }
        }).ask((short) permissions[0].hashCode());
    }

    public static PermissionUtil.PermissionRequestObject requestPermission(Fragment fragment,
                                                                           final String[] permissions,
                                                                           final PermissionCallBack callBack) {
        return PermissionUtil.with(fragment).request(permissions).onResult(new Func2() {
            @Override
            protected void call(int requestCode, String[] permissions, int[] grantResults) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        callBack.onGranted(permissions[i]);
                    } else {
                        callBack.onDenied(permissions[i]);
                    }
                }
            }
        }).ask((short) permissions[0].hashCode());
    }

    public static boolean hasPermission(AppCompatActivity activity, String permission) {
        return PermissionUtil.with(activity).has(permission);
    }

    public static boolean hasPermission(Fragment fragment, String permission) {
        return PermissionUtil.with(fragment).has(permission);
    }
}
