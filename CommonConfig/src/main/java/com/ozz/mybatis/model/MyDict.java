package com.ozz.mybatis.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("t_dict")
public class MyDict extends BaseObject {

  @TableId(type = IdType.AUTO)// 数据库ID自增
  private Long id;
  @TableField(value = "code", fill = FieldFill.INSERT_UPDATE)// 数据库中的字段名,
  private String code;
  @TableField(updateStrategy = FieldStrategy.IGNORED)// FieldStrategy.IGNORED:一定加入SQL,默认非NOT_NULL
  private String name;
  @TableField(exist = false)
  private String notExist;
}
