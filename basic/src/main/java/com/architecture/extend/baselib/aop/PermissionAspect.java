package com.architecture.extend.baselib.aop;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.mvvm.Viewable;
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
    @Pointcut("@annotation(com.architecture.extend.baselib.aop.NeedPermission)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object onActivityMethodAround(final ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        NeedPermission needPermission = signature.getMethod().getAnnotation(NeedPermission.class);
        if (needPermission == null) {
            return pjp.proceed();
        }
        String[] needPermissions = needPermission.permission();
        if (needPermissions.length == 0) {
            return pjp.proceed();
        }
        Object target = pjp.getTarget();
        if (!(target instanceof Viewable)) {
            return pjp.proceed();
        }
        RxPermissions rxPermissions = ((Viewable) target).getRxPermissions();
        rxPermissions.request(needPermissions).subscribe(isGranted -> {
            if (isGranted) {
                try {
                    pjp.proceed();
                } catch (Throwable throwable) {
                    throw new PermissionAspectException("origin method should not throw exception",
                            throwable);
                }
            }else{
                BaseApplication.getInstance().showApplicationInfo();
                ToastUtils.showShort(R.string.please_authorization);
            }
        });
        return null;
    }
}
