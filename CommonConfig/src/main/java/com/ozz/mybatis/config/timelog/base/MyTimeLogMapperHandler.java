package com.ozz.mybatis.config.timelog.base;

import cn.hutool.core.lang.Pair;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("rawtypes")
public class MyTimeLogMapperHandler implements InvocationHandler {
    Object target;
    Class targetClass;

    public MyTimeLogMapperHandler(Object target, Class targetClass) {
        this.target = target;
        this.targetClass = targetClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long time = System.nanoTime();
        Object obj = null;
        Throwable te = null;
        try {
            obj = method.invoke(this.target, args);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException && e.getCause() != null) {
                te = e.getCause();
            } else {
                te = e;
            }
        }
        MyTimeLog timeLog = MyTimeLog.get();
        if (timeLog != null) {
            String key = String.format("%s.%s()", this.targetClass.getName(), method.getName());
            Pair<AtomicInteger, AtomicLong> p = timeLog.getMapperLogMap().computeIfAbsent(key, t->Pair.of(new AtomicInteger(0), new AtomicLong(0)));
            time = System.nanoTime() - time;
            p.getKey().incrementAndGet();
            p.getValue().addAndGet(time);
        }
        if (te != null) {
            throw te;
        }
        return obj;
    }
}
