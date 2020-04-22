package com.ozz.mybatis.mapper.routing.datasource;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TestMapper {
  public List<String> selectTest();
  public int updateTest(@Param("ids") List<Long> ids);
  public Date selectTxTimestamp(long id);
}
