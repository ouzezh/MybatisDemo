package com.ozz.mybatis.mapper.routing.datasource;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ozz.mybatis.model.BaseObject;
import com.ozz.mybatis.model.TestObject;
import org.apache.ibatis.annotations.Param;

public interface TestMapper extends BaseMapper<TestObject> {
  public List<String> selectTest();
  public int updateTest(@Param("ids") List<Long> ids);
  public Date selectTxTimestamp(long id);
}
