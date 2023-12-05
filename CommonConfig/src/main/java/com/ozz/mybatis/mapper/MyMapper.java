package com.ozz.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ozz.mybatis.model.MyDict;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MyMapper extends BaseMapper<MyDict> {

  List<String> selectTest(@Param("ids") Long... ids);

  int updateTest(@Param("id") long id, @Param("name") String name);
}
