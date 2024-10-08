package com.ozz.mybatis.config.timelog;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 日志存储
 */
@Getter
@Setter
public class MyTimeLog {
    private static final ThreadLocal<MyTimeLog> MAIN_LOG = new ThreadLocal<>();// 当前线程日志

    // 代码执行时间 <methodPath, [invokeCount, costTime, isRunning]>
    private Map<String, Tuple> logMap = Collections.synchronizedMap(new LinkedHashMap<>());
    // mybatis执行时间 <methodPath, <invokeCount, costTime>>
    private Map<String, Pair<AtomicInteger, AtomicLong>> mapperLogMap = Collections.synchronizedMap(new LinkedHashMap<>());

    public static MyTimeLog get() {
        return MAIN_LOG.get();
    }

    public static MyTimeLog start() {
        MyTimeLog m = MAIN_LOG.get();
        if (m == null) {
            m = new MyTimeLog();
            MAIN_LOG.set(m);
        }
        return m;
    }

    public static MyTimeLog stop() {
        MyTimeLog m = MAIN_LOG.get();
        MAIN_LOG.remove();
        return m;
    }

}
