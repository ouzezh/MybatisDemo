package com.ozz.mybatis.service;

import cn.hutool.core.util.StrUtil;
import com.ozz.mybatis.mapper.MyMapper;
import com.ozz.mybatis.model.MyDict;
import com.ozz.mybatis.util.MyBaseTest;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
class MyServiceTest extends MyBaseTest {

  @Autowired
  MyService myService;
  @Autowired
  SqlSessionFactory sqlSessionFactory;

  @Test
  void testSelectJdbcTemplate() {
    println(myService.selectJdbcTemplate("select name from t_dict where id=?", 1L));
  }

  @Test
  void testSelect() {
    println(myService.selectPage());
    println(myService.selectPageInfo());
  }

  @Test
  void testSelectCursor() {
    try (SqlSession ss = sqlSessionFactory.openSession()) {
      try (Cursor<MyDict> cursor = ss
          .selectCursor(String.format("%s.selectList", MyMapper.class.getName()))) {
        for (MyDict o : cursor) {
          println(o);
        }
        PreparedStatement a;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Test
  void testUpdate() throws Exception {
    long id = 1;
    String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HHmmss SSS"));
    List<String> list = selectName(id);
    Assert.isTrue(list.size()==1&&!name.equals(list.get(0)), StrUtil.EMPTY);
    myService.updateTest(id, name);
    list = selectName(id);
    Assert.isTrue(list.size()==1&&name.equals(list.get(0)), StrUtil.EMPTY);
  }

  @Test
  void testTxUpdate() throws Exception {
    long id = 2;
    String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HHmmss SSS"));
    List<String> list = selectName(id);
    Assert.isTrue(list.size()==1&&!name.equals(list.get(0)), StrUtil.EMPTY);
    try {
      myService.updateTxTest(id, name);
    } catch (RuntimeException e) {
      if("MyException".equals(e.getMessage())) {
        e.printStackTrace();
      } else {
        throw e;
      }
    }
    List<String> list2 = selectName(id);
    Assert.isTrue(list2.size()==1&&list2.get(0).equals(list.get(0)), StrUtil.EMPTY);
  }

  private List<String> selectName(long id) {
    return myService.selectJdbcTemplate("select name from t_dict where id=?", id);
  }
}
