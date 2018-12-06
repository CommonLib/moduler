package com.architecture.extend.baselib.aop;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.exception.BaseException;
import com.architecture.extend.baselib.mvvm.RequestPermissionAble;
import com.architecture.extend.baselib.util.VerifyUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


/**
 * Created by byang059 on 2018/6/20.
 */

@Aspect
public class PermissionAspect {
    @Pointcut("execution(@com.architecture.extend.baselib.aop.NeedPermission void *(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public void onActivityMethodAround(ProceedingJoinPoint pjp) throws Throwable {
        Object target = pjp.getTarget();
        assertAssignFromPermissionAble(target);

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        NeedPermission needPermission = signature.getMethod().getAnnotation(NeedPermission.class);
        String[] permission = needPermission.permission();
        if (permission.length == 0) {
            pjp.proceed();
        }

        RequestPermissionAble requestPermission = (RequestPermissionAble) target;
        RxPermissions rxPermissions = requestPermission.getRxPermissions();
        VerifyUtil.assertNotNull(rxPermissions);

        requestPermission(pjp, permission, rxPermissions);
    }

    private void requestPermission(ProceedingJoinPoint pjp, String[] permission,
                                   RxPermissions rxPermissions) {
        rxPermissions.request(permission).subscribe(isGranted -> {
            if (isGranted) {
                try {
                    pjp.proceed();
                } catch (Throwable t) {
                    throw new BaseException(t);
                }
            } else {
                BaseApplication.getInstance().showApplicationInfo();
                ToastUtils.showShort(R.string.please_authorization);
            }
        });
    }

    private static void assertAssignFromPermissionAble(Object target) {
        if (!(target instanceof RequestPermissionAble)) {
            throw new ClassCastException();
        }
    }
}
