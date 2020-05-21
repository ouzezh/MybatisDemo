package com.ozz.mybatis.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestServiceTest {

  @Autowired
  private ITestService testService;

  @Test
  public void testPage() {
    testService.page(1, 10);
  }

}
