package com.ozz.mybatis.config;

import com.ozz.mybatis.config.db.MyDynamicDS;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 配置多数据源
 *
 */
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class MyDynamicDSConfig {
  @Bean(MyDynamicDS.DS)
  @ConfigurationProperties("spring.datasource.dynamic.datasource.ds")
  public DataSource ds() {
    DataSource dataSource = DataSourceBuilder.create().build();
    return dataSource;
  }

  @Bean(MyDynamicDS.DS2)
  @ConfigurationProperties("spring.datasource.dynamic.datasource.ds2")
  public DataSource ds2() {
    DataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
    return dataSource;
  }

  @Bean
  @Primary
  public MyDynamicDS dynamicDataSource(@Qualifier("ds") DataSource ds, DataSource ds2) {
    Map<Object, Object> targetDataSources = new HashMap<>(2);
    targetDataSources.put(MyDynamicDS.DS, ds);
    targetDataSources.put(MyDynamicDS.DS2, ds2);
    MyDynamicDS dynamicDataSource = new MyDynamicDS();
    dynamicDataSource.setDefaultTargetDataSource(ds);
    dynamicDataSource.setTargetDataSources(targetDataSources);
    return dynamicDataSource;
  }
}
