package com.ozz.mybatis.config.aspect;

import com.ozz.mybatis.config.db.MyDynamicDSContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyDynamicDSAspect {

  @Pointcut("@annotation(com.ozz.mybatis.config.aspect.MyDS)")
  public void dsPointCut() {
  }

  @Around("dsPointCut()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    String dsKey = getDsKey(joinPoint);
    try {
      MyDynamicDSContextHolder.setContextKey(dsKey);
      return joinPoint.proceed();
    } finally {
        MyDynamicDSContextHolder.removeContextKey();
    }
  }

  private String getDsKey(ProceedingJoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    return methodSignature.getMethod().getAnnotation(MyDS.class).value();
  }
}