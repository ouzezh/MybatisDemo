package com.ozz.mybatis.model;

//import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ozz.mybatis.util.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public abstract class BaseObject {
//  @TableField(exist = false)
  @Override
  public String toString() {
    try {
      return JsonUtil.toJson(this);
    } catch (Exception e) {
      return super.toString();
    }
  }
}
