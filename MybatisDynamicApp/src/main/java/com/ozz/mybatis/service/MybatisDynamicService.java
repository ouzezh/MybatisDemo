package com.ozz.mybatis.service;

import com.ozz.mybatis.config.db.MyDynamicDS;
import com.ozz.mybatis.config.aspect.MyDS;
import com.ozz.mybatis.mapper.MyMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MybatisDynamicService {
  @Autowired
  MyMapper myMapper;

  @MyDS(MyDynamicDS.DS2)
  public List<String> selectDb2(long id) {
    return myMapper.selectTest(id);
  }

}
