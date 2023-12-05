package com.ozz.mybatis.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ozz.mybatis.mapper.MyMapper;
import com.ozz.mybatis.service.MyService;
import com.ozz.mybatis.util.MybatisUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
public class MyServiceImpl implements MyService {

  @Autowired
  private MyMapper myMapper;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<String> selectJdbcTemplate(String sql, Object... args) {
    List<String> resList = new ArrayList<>();
    RowCallbackHandler rc = new RowCallbackHandler() {
      @Override
      public void processRow(ResultSet rs) throws SQLException {
        resList.add(rs.getString(1));
      }
    };
    jdbcTemplate.query(sql, rc, args);
    return resList;
  }

  /**
   * 分页查询
   */
  @Override
  public Page<String> selectPage(Long... ids) {
    return MybatisUtil.selectPage(() -> myMapper.selectTest(ids), 1, 5, false);
  }

  /**
   * 分页查询（包含页面分页控制信息）
   */
  @Override
  public PageInfo<String> selectPageInfo(Long... ids) {
    return MybatisUtil.selectPageInfo(() -> myMapper.selectTest(ids), 1, 5);
  }

  @Override
  public int updateTest(long id, String name) {
    return myMapper.updateTest(id, name);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateTxTest(long id, String name) {
    int c = updateTest(id, name);
    if(c > 0) {
      throw new RuntimeException("MyException");
    }
  }

}
