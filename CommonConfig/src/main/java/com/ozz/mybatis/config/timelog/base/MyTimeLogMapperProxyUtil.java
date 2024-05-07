package com.ozz.mybatis.config.timelog.base;

import cn.hutool.core.lang.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class MyTimeLogMapperProxyUtil {

    public static <T> Object invoke(Class<T> mapperInterface, Method method, Supplier<Object> supplier) throws Throwable {
        long time = System.nanoTime();
        Object res = null;
        Throwable te = null;
        try {
            res = supplier.get();
        } catch (Exception e) {
            if (e instanceof InvocationTargetException && e.getCause() != null) {
                te = e.getCause();
            } else {
                te = e;
            }
        }
        MyTimeLog timeLog = MyTimeLog.get();
        if (timeLog != null) {
            String key = String.format("%s.%s()", mapperInterface.getName(), method.getName());
            Pair<AtomicInteger, AtomicLong> p = timeLog.getMapperLogMap().computeIfAbsent(key, t->Pair.of(new AtomicInteger(0), new AtomicLong(0)));
            time = System.nanoTime() - time;
            p.getKey().incrementAndGet();
            p.getValue().addAndGet(time);
        }
        if (te != null) {
            throw te;
        }
        return res;
    }
}
