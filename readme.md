## Springboot 数据库配置

[Springboot 连接池配置](https://docs.spring.io/spring-boot/docs/2.5.x/reference/html/spring-boot-features.html#boot-features-connect-to-production-database-configuration)

[Hikari 连接池配置](https://gitee.com/mirrors/hikaricp?_from=gitee_search#essentials)



## p6spy

```
  打印sql
  支持Simple Logging Facade For Java (SLF4J)，直接添加日志配置 spy.properties 文件即可
```



## MyBatis-Plus

IMybatisPlusService extends IService

[MyBatis-Plus 从入门到上手干事！](https://github.com/CodingDocs/springboot-guide/blob/master/docs/MyBatisPlus.md)

[MyBatis-Plus 指南](https://baomidou.com)

[MyBatis-Plus 动态数据源](https://baomidou.com/guide/dynamic-datasource.html)

[MyBatis-Plus 动态数据源 集成ShardingJdbc](https://www.kancloud.cn/tracy5546/dynamic-datasource/2268593)
 | [MyBatis-Plus集成ShardingJdbc样例](https://github.com/dynamic-datasource/dynamic-datasource-samples/tree/master/third-part-samples/sharding-jdbc-sample)
```
1.为什么直接调用某些方法切换数据源失败❓
mp内置的ServiceImpl在新增,更改,删除等一些方法上自带事物导致不能切换数据源。
解决办法:写自己的service方法,调用baseMapper的方法。
```