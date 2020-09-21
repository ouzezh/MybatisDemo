package com.ozz.mybatis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("city")
public class TestObject extends BaseObject {
//  @TableField(exist = false)// 属性在数据库中不存在
//  @TableField("id")// 属性对应字段名

  @TableId(type = IdType.AUTO)// 数据库ID自增
  private Integer id;
}
