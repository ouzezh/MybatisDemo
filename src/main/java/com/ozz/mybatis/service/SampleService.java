package com.ozz.mybatis.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ozz.mybatis.config.db.routing.RoutingDataSource;
import com.ozz.mybatis.config.db.routing.TargetDataSource;
import com.ozz.mybatis.mapper.routing.datasource.TestMapper;
import com.ozz.mybatis.mapper.routing.datasource2.Test2Mapper;
import com.ozz.mybatis.mapper.second.TestSecondMapper;
import com.ozz.mybatis.util.MybatisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
//@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
public class SampleService {
  @Autowired
  private TestMapper testMapper;
  @Autowired
  private Test2Mapper test2Mapper;
  @Autowired
  private TestSecondMapper testSecondMapper;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<String> selectJdbcTemplate() {
    List<String> resList = new ArrayList<>();
    RowCallbackHandler rc = new RowCallbackHandler() {
      @Override
      public void processRow(ResultSet rs) throws SQLException {
        resList.add(rs.getString(1));
      }
    };
    jdbcTemplate.query("select 1", rc);
    return resList;
  }

  /**
   * 分页查询
   */
  public PageInfo<String> selectPage() {
    PageInfo<String> page = MybatisUtil.selectPageInfo(() -> testMapper.selectTest(), 1, 5);
    return page;
  }

  /**
   * 分页查询
   */
  @TargetDataSource(RoutingDataSource.DATASOURCE2)
  public Page<String> selectPage2() {
    Page<String> page = MybatisUtil.selectPage(() -> test2Mapper.selectTest2(), 1, 1, false);
    return page;
  }

  public Page<String> selectPageSecond() {
    Page<String> page = MybatisUtil.selectPage(() -> testSecondMapper.selectTest2(), 1, 1, false);
    return page;
  }

  public Page<String> readWriteSplit() {
    Page<String> page = MybatisUtil.selectPage(() -> test2Mapper.selectTest2(), 1, 1, false);
    return page;
  }

  /**
   * 更新
   */
  public int updateTest(long id) {
    return testMapper.updateTest(Collections.singletonList(id));
  }

  public void selectTxAdviceTest(long id) throws Exception {
    try {
      testTxAdvice(id);
    } catch (UnsupportedOperationException e) {
      throw new UnsupportedOperationException("未启用事务，请验记录更新");
    }
  }

  // 目前使用aspect实现，可使用注解实现
  //  @Transactional(value = "secondTransactionManager", rollbackFor = Exception.class)
  public void updateTxAdviceTest(long id) throws Exception {
    try {
      testTxAdvice(id);
    } catch (UnsupportedOperationException e) {
      throw new UnsupportedOperationException("启用事务，请验证操作回滚");
    }
  }

  private void testTxAdvice(long id) {
    int c = testMapper.updateTest(Collections.singletonList(id));
    if(c != 1) {
      throw new RuntimeException(String.format("更新的条数为空:%d",id));
    }
    throw new UnsupportedOperationException("test transaction");
  }
  public boolean checkTxUpdated(long id) {
    Date ts = testMapper.selectTxTimestamp(id);
    if(ts == null) {
      return false;
    } else {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ts);
      cal.add(Calendar.SECOND, 15);
      return cal.getTime().after(new Date());
    }
  }
}
