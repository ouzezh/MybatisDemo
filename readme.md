## 多数据源支持
```
  application.yml

  com.ozz.mybatis.component.db.routing.*

  MybatisApp
    @Import({RoutingDataSourceConfig.class})
    @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
```

## log4jdbc
```
  打印sql
  支持Simple Logging Facade For Java (SLF4J)，直接添加日志配置log4jdbc.properties文件即可
```

## MyBatis-Plus
  IMybatisPlusService extends IService

