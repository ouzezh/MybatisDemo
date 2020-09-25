package com.ozz.mybatis.config.db.second;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 自动切换数据源：根据basePackages及MapperLocation包路径
 *
 */
@Configuration
@MapperScan(basePackages = SecondDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "secondSqlSessionFactory")
public class SecondDataSourceConfig {

  static final String PACKAGE = "com.ozz.mybatis.mapper.second";
  static final String MAPPER_LOCATION = "classpath*:mapper/second/**/*.xml";

  @Bean(name = "secondDataSource")
  @ConfigurationProperties("spring.datasource.datasource2")
  public DataSource secondDataSource() {
    System.setProperty("log4jdbc.auto.load.popular.drivers", "false");
    System.setProperty("log4jdbc.drivers", com.mysql.cj.jdbc.Driver.class.getName());

    DataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
    return dataSource;
  }

  @Bean(name = "secondTransactionManager")
  public DataSourceTransactionManager secondTransactionManager(
      @Qualifier("secondDataSource") DataSource secondDataSource) {
    return new DataSourceTransactionManager(secondDataSource);
  }

  @Bean(name = "secondSqlSessionFactory")
  public SqlSessionFactory secondSqlSessionFactory(@Qualifier("secondDataSource") DataSource secondDataSource) throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(secondDataSource);
    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources(MAPPER_LOCATION));
    sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
    return sessionFactory.getObject();
    // mybatis plus support see master datasource config
  }
}
