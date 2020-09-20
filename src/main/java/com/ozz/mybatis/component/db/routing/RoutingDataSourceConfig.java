package com.ozz.mybatis.component.db.routing;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 配置多数据源
 *
 * 方式一：扫描所有MapperLocation，使用Aspect切换AbstractRoutingDataSource来源
 *
 * 方式二(推荐)：扫描不同basePackages及MapperLocation的路径，使用对应接口自动切换数据源
 *
 */
@Configuration
@MapperScan(basePackages=RoutingDataSourceConfig.PACKAGE, sqlSessionFactoryRef="masterSqlSessionFactory")
public class RoutingDataSourceConfig {
  static final String PACKAGE = "com.ozz.mybatis.mapper.routing";
  static final String MAPPER_LOCATION = "classpath*:mapper/routing/**/*.xml";

  @Bean
  @ConfigurationProperties("spring.datasource.datasource1")
  public DataSource dataSource1() {
    System.setProperty("log4jdbc.auto.load.popular.drivers", "false");
    System.setProperty("log4jdbc.drivers", com.mysql.cj.jdbc.Driver.class.getName());

    DataSource dataSource = DataSourceBuilder.create().build();
    return dataSource;
  }

  @Bean
  @ConfigurationProperties("spring.datasource.datasource2")
  public DataSource dataSource2() {
    System.setProperty("log4jdbc.auto.load.popular.drivers", "false");
    System.setProperty("log4jdbc.drivers", com.mysql.cj.jdbc.Driver.class.getName());

    DataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
    return dataSource;
  }

  @Bean
  @Primary
  public RoutingDataSource routingSource(@Qualifier("dataSource1") DataSource dataSource, DataSource dataSource2) {
    Map<Object, Object> targetDataSources = new HashMap<>(2);
    targetDataSources.put(RoutingDataSource.DATASOURCE1, dataSource);
    targetDataSources.put(RoutingDataSource.DATASOURCE2, dataSource2);
    return new RoutingDataSource(dataSource, targetDataSources);
  }

  /**
   * just for SecondDataSourceConfig
   */
  @Bean(name = "masterTransactionManager")
  @Primary
  public DataSourceTransactionManager masterTransactionManager(RoutingDataSource routingSource) {
    return new DataSourceTransactionManager(routingSource);
  }

  /**
   * just for SecondDataSourceConfig
   */
  @Bean(name = "masterSqlSessionFactory")
  @Primary
  public SqlSessionFactory masterSqlSessionFactory(RoutingDataSource routingSource, PaginationInterceptor paginationInterceptor)  throws Exception {
    // MyBatis 配置
//    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//    sessionFactory.setDataSource(routingSource);
//    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
//        .getResources(MAPPER_LOCATION));
//    sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
//    return sessionFactory.getObject();

    // MyBatis-Plus 配置
    MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
    sqlSessionFactory.setDataSource(routingSource);
    sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));

    MybatisConfiguration configuration = new MybatisConfiguration();
//        configuration.setJdbcTypeForNull(JdbcType.NULL);
    configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setCacheEnabled(false);
    sqlSessionFactory.setConfiguration(configuration);
    sqlSessionFactory.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
        paginationInterceptor //添加分页功能
    });
    //sqlSessionFactory.setGlobalConfig(globalConfiguration());
    return sqlSessionFactory.getObject();
  }

  @Bean
  public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
  }

  @Bean
  public OracleKeyGenerator oracleKeyGenerator(){
    return new OracleKeyGenerator();
  }
}
