package com.ozz.mybatis.config.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Optional;

@Slf4j
public class MyDynamicDS extends AbstractRoutingDataSource {
  public static final String DS = "ds";
  public static final String DS2 = "ds2";

  @Override
  protected Object determineCurrentLookupKey() {
    String key = MyDynamicDSContextHolder.getContextKey();
    log.info("switch dataSource to {}", Optional.ofNullable(key).orElseGet(()->DS));
    return key;
  }
}
