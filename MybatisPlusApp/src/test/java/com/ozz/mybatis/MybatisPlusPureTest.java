package com.ozz.mybatis;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ozz.mybatis.pure.mapper.MyPureMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class MybatisPlusPureTest {
    public static void main(String[] args) throws Exception {
        new MybatisPlusPureTest().test();
    }

    private void test() throws Exception {
        try (CloseableSqlSessionFactory ssf = sqlSessionFactory("jdbc:p6spy:sqlite:C:\\Dev\\SQLite\\demo_db.db", null
                , null
                , "com.p6spy.engine.spy.P6SpyDriver", "classpath*:pure/**/*.xml")) {
            try (SqlSession ss = ssf.ssf.openSession()) {
                MyPureMapper myMapper = ss.getMapper(MyPureMapper.class);
                List<String> list = myMapper.selectTest(1L);
                System.out.println(JSONUtil.toJsonStr(list));
                Assert.isTrue(list.size() > 0);
            }
            ssf.close();
            try (SqlSession ss = ssf.ssf.openSession()) {
                MyPureMapper myMapper = ss.getMapper(MyPureMapper.class);
                myMapper.selectTest(1L);
                throw ExceptionUtil.wrapRuntime("数据库连接池已关闭任能打开SqlSession");
            } catch (Exception e) {
                if(e.getMessage().contains("has been closed")) {
                    System.out.println(e.getMessage());
                } else {
                    throw e;
                }
            }
        }
    }

    public CloseableSqlSessionFactory sqlSessionFactory(String url, String user, String pwd, String driver,
                                                        String mapperLocation) throws Exception {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        if (StrUtil.isNotEmpty(user)) {
            dataSource.setUsername(user);
        }
        if (StrUtil.isNotEmpty(pwd)) {
            dataSource.setPassword(pwd);
        }
        if (StrUtil.isNotEmpty(driver)) {
            dataSource.setDriverClassName(driver);
        }
        dataSource.setMaximumPoolSize(3);
        dataSource.setMaxLifetime(1500000L);

        Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
        System.out.println(JSONUtil.toJsonStr(mapperLocations));
        Assert.isTrue(mapperLocations.length > 0);

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(mapperLocations);
        SqlSessionFactory ssf = sessionFactory.getObject();
        assert ssf != null;
        ssf.getConfiguration().setMapUnderscoreToCamelCase(true);
        return new CloseableSqlSessionFactory(ssf, dataSource);

//            // 注意：不要在Spring项目中手工创建MyBatisPlus的SqlSession，数据源可能会串
//            MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//            sqlSessionFactory.setDataSource(dataSource);
//            sqlSessionFactory.setMapperLocations(mapperLocations);
//
//            MybatisConfiguration configuration = new MybatisConfiguration();
//            configuration.setMapUnderscoreToCamelCase(true);
//            sqlSessionFactory.setConfiguration(configuration);
////        sqlSessionFactory.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
////                paginationInterceptor //添加分页功能
////        });
//            return new CloseableSqlSessionFactory(sqlSessionFactory.getObject(), dataSource);
    }

    public class CloseableSqlSessionFactory implements Closeable {

        public SqlSessionFactory ssf;
        public HikariDataSource dataSource;

        public CloseableSqlSessionFactory(SqlSessionFactory ssf, HikariDataSource dataSource) {
            this.ssf = ssf;
            this.dataSource = dataSource;
        }

        @Override
        public void close() throws IOException {
            dataSource.close();
        }
    }
}
