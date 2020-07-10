package com.ozz.mybatis.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MybatisPlusServiceTest {

  @Autowired
  private IMybatisPlusService mybatisPlusService;

  @Test
  public void testPage() {
    mybatisPlusService.page(1, 10);
  }

}
