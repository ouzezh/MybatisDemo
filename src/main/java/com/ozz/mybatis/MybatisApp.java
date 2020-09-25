package com.ozz.mybatis;

import com.ozz.mybatis.config.db.routing.RoutingDataSourceConfig;
import com.ozz.mybatis.config.db.second.SecondDataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

// 动态数据源配置,需要将自有的配置依赖(DynamicDataSourceConfig),将原有的依赖去除(DataSourceAutoConfiguration)
@Import({RoutingDataSourceConfig.class, SecondDataSourceConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

//// 单数据源配置
//@MapperScan("com.ozz.mybatis.mapper")

// @ImportResource(locations={"classpath:mybatis/spring-context.xml"})
public class MybatisApp {

  public static void main(String[] args) {
    SpringApplication.run(MybatisApp.class, args);
  }

}
