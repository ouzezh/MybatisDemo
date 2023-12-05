package com.ozz.mybatis.config.db;

public class MyDynamicDSContextHolder {

  /**
   * 动态数据源名称上下文
   */
  private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

  /**
   * 设置/切换数据源
   */
  public static void setContextKey(String key) {
    contextHolder.set(key);
  }

  /**
   * 获取数据源名称
   */
  public static String getContextKey() {
    return contextHolder.get();
  }

  /**
   * 删除当前数据源名称
   */
  public static void removeContextKey() {
    contextHolder.remove();
  }
}