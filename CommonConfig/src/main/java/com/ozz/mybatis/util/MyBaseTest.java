package com.ozz.mybatis.util;

import cn.hutool.log.StaticLog;

public abstract class MyBaseTest {
    public void println(Object msg) {
        StaticLog.info(String.format("[MyTest] %s", msg.toString()));
    }
}
