## Spring动态据源源
```
  扫描所有MapperLocation，使用Aspect切换AbstractRoutingDataSource来源

  application.yml

  com.ozz.mybatis.component.db.routing.*

  MybatisApp
    @Import({RoutingDataSourceConfig.class})
    @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
```

## 访问多个数据库
```
扫描不同basePackages及MapperLocation的路径，使用对应接口自动切换数据源

@Import({RoutingDataSourceConfig.class, SecondDataSourceConfig.class})
配置多个数据源，分别使用不同的mapper文件和model类
```

## log4jdbc
```
  打印sql
  支持Simple Logging Facade For Java (SLF4J)，直接添加日志配置log4jdbc.properties文件即可
```

## MyBatis-Plus
  IMybatisPlusService extends IService

