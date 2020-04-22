package com.ozz.mybatis.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ozz.mybatis.service.SampleService;

@RestController
public class SampleController {
  @Autowired
  private SampleService testService;

  @RequestMapping(value = "/v1/test/jdbctemplate")
  public List<String> testSelectJdbcTemplate() {
    return testService.selectJdbcTemplate();
  }

  @RequestMapping(value = "/v1/test/select")
  public PageInfo<String> testSelect() {
    return testService.selectPage();
  }

  @RequestMapping(value = "/v1/test/select2")
  public Page<String> testSelect2() {
    return testService.selectPage2();
  }

  @RequestMapping(value = "/v1/test/update")
  public int testUpdate() {
    return testService.updateTest(1L);
  }

  @RequestMapping(value = "/v1/test/txadvice/select")
  public void testTxAdviceSelect() throws Exception {
    testService.selectTxAdviceTest(2);
  }

  @RequestMapping(value = "/v1/test/txadvice/update")
  public void testTxAdviceUpdate() throws Exception {
    testService.updateTxAdviceTest(3);
  }
}
