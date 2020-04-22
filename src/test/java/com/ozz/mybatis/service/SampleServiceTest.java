package com.ozz.mybatis.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class SampleServiceTest {

  @Autowired
  private SampleService testService;

  @Test
  public void testSelectJdbcTemplate() {
    System.out.println(testService.selectJdbcTemplate());
  }

  @Test
  public void testSelect() {
    PageInfo<String> res = testService.selectPage();
    System.out.println(res);
  }

  @Test
  public void testSelect2() {
    Page<String> res = testService.selectPage2();
    System.out.println(res);
  }

  @Test
  public void testSelectSecond() {
    Page<String> res = testService.selectPageSecond();
    System.out.println(res);
  }

  @Test
  public void testReadWriteSplit() {
    Page<String> res = testService.readWriteSplit();
    System.out.println(res);
  }

  @Test
  public void testUpdate() {
    int res = testService.updateTest(1L);
    System.out.println(res);
  }

  @Test
  public void testTxAdviceSelect() throws Exception {
    long id = 1;
    Assert.state(!testService.checkTxUpdated(id), "previous state error");
    try {
      testService.selectTxAdviceTest(id);
    } catch (UnsupportedOperationException e) {
      System.out.println(e.getMessage());
    }
    Assert.state(testService.checkTxUpdated(id), "record change error");
  }

  @Test
  public void testTxAdviceUpdate() throws Exception {
    long id = 2;
    Assert.state(!testService.checkTxUpdated(id), "previous state error");
    try {
      testService.updateTxAdviceTest(id);
    } catch (UnsupportedOperationException e) {
      System.out.println(e.getMessage());
    }
    Assert.state(!testService.checkTxUpdated(id), "record rollback error");
  }

}
