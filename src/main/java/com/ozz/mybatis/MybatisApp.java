package com.ozz.mybatis;

import com.ozz.mybatis.component.db.second.SecondDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.ozz.mybatis.component.db.routing.RoutingDataSourceConfig;

// 动态数据源配置,需要将自有的配置依赖(DynamicDataSourceConfig),将原有的依赖去除(DataSourceAutoConfiguration)
@Import({RoutingDataSourceConfig.class, SecondDataSourceConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

// @ImportResource(locations={"classpath:mybatis/spring-context.xml"})
public class MybatisApp {

  public static void main(String[] args) {
    SpringApplication.run(MybatisApp.class, args);
  }

}
