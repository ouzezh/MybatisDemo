package com.ozz.mybatis.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ozz.mybatis.mapper.MyMapper;
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

public interface MyService {
  public List<String> selectJdbcTemplate(String sql, Object... args);

  /**
   * 分页查询
   */
  public Page<String> selectPage(Long... ids);

  /**
   * 分页查询（包含页面分页控制信息）
   */
  public PageInfo<String> selectPageInfo(Long... ids);

  public int updateTest(long id, String name);

  @Transactional(rollbackFor = Exception.class)
  public void updateTxTest(long id, String name);

}
