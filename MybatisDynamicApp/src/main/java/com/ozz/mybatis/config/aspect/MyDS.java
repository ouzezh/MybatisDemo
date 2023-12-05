package com.ozz.mybatis.config.aspect;

import com.ozz.mybatis.config.db.MyDynamicDS;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyDS {
  String value() default MyDynamicDS.DS;
}