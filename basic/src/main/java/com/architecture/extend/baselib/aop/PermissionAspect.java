package com.architecture.extend.baselib.aop;

import com.architecture.extend.baselib.util.LogUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by byang059 on 2018/6/20.
 */

@Aspect
public class PermissionAspect {
    @Pointcut("@annotation(com.architecture.extend.baselib.aop.Permission)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public void onActivityMethodAround(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        long start = System.currentTimeMillis();
        pjp.proceed();
        long end = System.currentTimeMillis();
        LogUtil.d(targetMethod.getName() + " execute time:" + (end - start) + "ms");
    }
}
