package com.architecture.extend.baselib.aop;

import com.architecture.extend.baselib.BaseApplication;
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
public class DebugAspect {
    @Pointcut("@annotation(com.architecture.extend.baselib.aop.DebugLog)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public void onActivityMethodAround(ProceedingJoinPoint pjp) throws Throwable {
        if (BaseApplication.isDebug) {
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            long start = System.currentTimeMillis();
            pjp.proceed();
            long end = System.currentTimeMillis();
            String[] parameterNames = methodSignature.getParameterNames();
            Object[] args = pjp.getArgs();
            StringBuilder sb = new StringBuilder();
            sb.append("invoke method ").append(targetMethod.getDeclaringClass().getSimpleName())
                    .append(".").append(targetMethod.getName()).append("()").append(", ");
            for (int i = 0; i < args.length; i++) {
                sb.append(parameterNames[i]).append(" = ").append(args[0]).append(",");
            }
            sb.append(" execute cost time: ").append(end - start).append("ms");
            LogUtil.d(sb.toString());
        } else {
            pjp.proceed();
        }
    }
}
