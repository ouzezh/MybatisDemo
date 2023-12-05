package com.ozz.mybatis.config.db;

import cn.hutool.log.StaticLog;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Optional;

public class MyDynamicDS extends AbstractRoutingDataSource {
  public static final String DS = "ds";
  public static final String DS2 = "ds2";

  @Override
  protected Object determineCurrentLookupKey() {
    String key = MyDynamicDSContextHolder.getContextKey();
    StaticLog.info("switch dataSource to {}", Optional.ofNullable(key).orElseGet(()->DS));
    return key;
  }
}
