package com.ozz.mybatis.config.db.routing_xml;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.ozz.mybatis.config.db.routing.RoutingDataSource;
import com.ozz.mybatis.config.db.routing.TargetDataSource;

public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
  @Override
  public void before(Method method, Object[] args, Object target) throws Throwable {
    if (method.isAnnotationPresent(TargetDataSource.class)) {
      TargetDataSource dataSource = method.getAnnotation(TargetDataSource.class);
      RoutingDataSource.setDataSource(dataSource.value());
    } else {
      RoutingDataSource.clearDataSource();
    }
  }

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    RoutingDataSource.clearDataSource();
  }
}
