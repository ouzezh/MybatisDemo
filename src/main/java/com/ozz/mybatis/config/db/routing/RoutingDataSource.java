package com.ozz.mybatis.config.db.routing;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
  public static final String DATASOURCE1 = "dataSource1";
  public static final String DATASOURCE2 = "dataSource2";

  private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

  public RoutingDataSource() {}
  public RoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
    super.setDefaultTargetDataSource(defaultTargetDataSource);
    super.setTargetDataSources(targetDataSources);
    super.afterPropertiesSet();
  }

  public static String getDataSource() {
    return contextHolder.get();
  }

  public static void setDataSource(String dataSource) {
    contextHolder.set(dataSource);
  }

  public static void clearDataSource() {
    contextHolder.remove();
  }

  @Override
  protected Object determineCurrentLookupKey() {
    return getDataSource();
  }
}
