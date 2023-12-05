package com.ozz.mybatis.service;

import cn.hutool.log.StaticLog;
import org.springframework.stereotype.Service;

@Service
public class MyMailService {
  public void sendErrorMail(String subject, String content, Throwable e) {
    StaticLog.info(String.format("send mail skip : %s", subject));
  }
}
