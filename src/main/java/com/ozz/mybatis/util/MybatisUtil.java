package com.ozz.mybatis.util;


import org.apache.ibatis.session.SqlSession;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Mybatis工具方法
 * 
 * 注：分页方法支持很多方式，参考：https://github.com/pagehelper/Mybatis-PageHelper
 */
public class MybatisUtil {
  /**
   * 获取mybatis接口的实例
   */
  public static <T> T getMapper(SqlSession s, Class<T> cls) {
    return s.getMapper(cls);
  }

  /**
   * 查询（返回页面...）
   */
  public static <T> PageInfo<T> selectPageInfo(ISelect select, int pageNum, int pageSize) {
    Page<T> page = selectPage(select, pageNum, pageSize, true);
    return page.toPageInfo();
  }

  /**
   * 高级查询（导出...）
   * @param pageNum 页码(最小为1)
   */
  public static <T> Page<T> selectPage(ISelect select, int pageNum, int pageSize, boolean count) {
    try {
      Page<T> page = PageHelper.startPage(pageNum, pageSize, count).doSelectPage(select);
      return page;
    } finally {
      PageHelper.clearPage();
    }
  }
}
