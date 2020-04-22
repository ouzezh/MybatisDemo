package com.ozz.mybatis.util;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用jackson框架实现json、对象转化
 * 
 * 注解忽略不可识别的属性注解 @JsonIgnoreProperties(ignoreUnknown = true)
 * 
 * 注解忽略空值 @JsonInclude(Include.NON_NULL)
 * 
 * @author ouzezh
 *
 *
 */
public class JsonUtil {
  private static ThreadLocal<ObjectMapper> objectMapper = new ThreadLocal<ObjectMapper>() {
    @Override
    protected ObjectMapper initialValue() {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));// 日期格式
      return objectMapper;
    }
  };

  public static String toJson(Object bean) {
    try {
      return objectMapper.get().writeValueAsString(bean);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T formJson(String json, Class<T> c) {
    try {
      return objectMapper.get().readValue(json, c);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 解析复杂格式
   */
  public <T> T formJson(String json, TypeReference<T> typeReference) {
    try {
      return objectMapper.get().readValue(json, typeReference);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
