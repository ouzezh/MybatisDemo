package com.ozz.mybatis;

import com.ozz.mybatis.component.db.routing.RoutingDataSource;
import com.ozz.mybatis.mapper.routing.datasource.TestMapper;
import com.ozz.mybatis.mapper.routing.datasource2.Test2Mapper;
import com.ozz.mybatis.util.DbUtil;
import com.ozz.mybatis.util.MybatisUtil;
import java.util.Collections;
import org.apache.ibatis.session.SqlSession;

public class MybatisDemo {
  public static void main(String[] args) {
    // 切换数据源
    RoutingDataSource.setDataSource(RoutingDataSource.DATASOURCE2);
    try (SqlSession s = DbUtil.openSession()) {
      Test2Mapper mapper = MybatisUtil.getMapper(s, Test2Mapper.class);
      MybatisUtil.selectPageInfo(()->mapper.selectTest2(), 1, 5);
    } finally {
      RoutingDataSource.clearDataSource();
    }

    try (SqlSession s = DbUtil.openSession()) {
      TestMapper mapper = MybatisUtil.getMapper(s, TestMapper.class);

      // 更新
      mapper.updateTest(Collections.singletonList(1l));
      DbUtil.commitSession(s);

      // 分页查询
      MybatisUtil.selectPage(()->mapper.selectTest(), 1, 5, false);
    }

    DbUtil.closeApplicationContext();
  }
}
