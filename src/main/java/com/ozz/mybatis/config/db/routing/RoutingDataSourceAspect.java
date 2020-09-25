package com.ozz.mybatis.config.db.routing;

import java.lang.reflect.Method;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 数据源AOP切面处理
 */
@Aspect
@Component
public class RoutingDataSourceAspect implements Ordered {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 基于注解切换数据源
   */
  @Pointcut("@annotation(com.ozz.mybatis.config.db.routing.TargetDataSource)")
  public void dataSourcePointCut() {
  }

  @Around("dataSourcePointCut()")
  public Object aroundAnnotation(ProceedingJoinPoint point) throws Throwable {
    try {
      MethodSignature signature = (MethodSignature) point.getSignature();
      Method method = signature.getMethod();
      TargetDataSource ds = method.getAnnotation(TargetDataSource.class);

      RoutingDataSource.setDataSource(ds.value());
      return point.proceed();
    } finally {
      RoutingDataSource.clearDataSource();
    }
  }

  @Pointcut("execution(public * com.ozz.mybatis.service..*.*(..))")
  public void serviePointcut() {
  }

  /**
   * 基于方法名切换数据源，可用于配置读写分离
   */
  @Around("serviePointcut()")
  public Object readWriteSplit(ProceedingJoinPoint point) throws Throwable {
      MethodSignature signature = (MethodSignature) point.getSignature();
      Method method = signature.getMethod();
      if (!method.isAnnotationPresent(TargetDataSource.class)) {
        if (Arrays.stream(new String[]{"readWrite"}).anyMatch((t) -> method.getName().startsWith(t))) {
          try {
            RoutingDataSource.setDataSource(RoutingDataSource.DATASOURCE2);
            return point.proceed();
          } finally {
            RoutingDataSource.clearDataSource();
          }
        }
      }
      return point.proceed();
  }

  @Override
  public int getOrder() {
    return 1;
  }
}
