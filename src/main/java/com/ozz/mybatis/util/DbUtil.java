package com.ozz.mybatis.util;
import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DbUtil {
  private static AbstractApplicationContext ac;
  private static SqlSessionFactory ssf;

  static {
    System.setProperty("log4jdbc.auto.load.popular.drivers", "false");
    System.setProperty("log4jdbc.drivers", com.mysql.cj.jdbc.Driver.class.getName());

    String conf = "classpath:/mybatis/spring-context.xml";
    ac = new ClassPathXmlApplicationContext(conf);
    ac.registerShutdownHook();
    ssf = (SqlSessionFactory) ac.getBean("sqlSessionFactory");

    Runtime.getRuntime().addShutdownHook(new Thread(()->DbUtil.closeApplicationContext()));
  }

  public static SqlSession openSession() {
    SqlSession s = ssf.openSession();
    Connection conn = s.getConnection();
    try {
      if (conn.getAutoCommit()) {
        conn.setAutoCommit(false);
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return s;
  }

  public static void commitSession(SqlSession s) {
    try {
      s.getConnection().commit();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void closeApplicationContext() {
    ac.close();
  }
}
