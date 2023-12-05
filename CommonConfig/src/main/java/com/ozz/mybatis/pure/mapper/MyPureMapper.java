package com.ozz.mybatis.pure.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyPureMapper {

  List<String> selectTest(@Param("ids") Long... ids);

}
