20190805 多数据源支持
  application.yml
  com.ozz.mybatis.component.db.routing.*
  MybatisApp
    @Import({DynamicDataSourceConfig.class})
    @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

20191009 log4jdbc
  打印sql
  支持Simple Logging Facade For Java (SLF4J)，直接添加日志配置文件即可