package com.ozz.mybatis.util;

public abstract class MyBaseTest {
    public void println(Object msg) {
        System.out.println(String.format("[MyTest] %s", msg.toString()));
    }
}
