package com.ozz.mybatis.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("city")
public class TestObject extends BaseObject {
    //  @TableField(exist = false)

    @TableField("id")
    private Integer id;
}
