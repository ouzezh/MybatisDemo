package com.ozz.mybatis.model;

import com.ozz.mybatis.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseObject {
  @Override
  public String toString() {
    try {
      return JsonUtil.toJson(this);
    } catch (Exception e) {
      return super.toString();
    }
  }
}
