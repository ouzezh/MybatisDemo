package com.ozz.mybatis.config.timelog.base;

import cn.hutool.core.lang.Pair;
import cn.hutool.log.StaticLog;
import com.ozz.mybatis.config.timelog.MyTimeLogAdvisor;
import com.ozz.mybatis.service.MyMailService;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 方法代理
 */
@Getter
@Setter
public class MyTimeLogInterceptor implements MethodInterceptor {
    private MyMailService myMailService;

    private void buildLogStr(StringBuilder sb, MyTimeLog timeLog, long totalTime) {
        if(timeLog == null) {
            return;
        }
        if(!timeLog.getLogMap().isEmpty()) {
            sb.append(System.lineSeparator()).append("--main--");
            for (Map.Entry<String, Pair<AtomicInteger, AtomicLong>> en : timeLog.getLogMap().entrySet()) {
                buildLogStr(sb, en, totalTime);
            }
        }
        if (!timeLog.getMapperLogMap().isEmpty()) {
            sb.append(System.lineSeparator()).append("--mapper--");
            Pair<AtomicInteger, AtomicLong> total = Pair.of(new AtomicInteger(0), new AtomicLong(0));
            for (Map.Entry<String, Pair<AtomicInteger, AtomicLong>> en : timeLog.getMapperLogMap().entrySet()) {
                total.getKey().addAndGet(en.getValue().getKey().get());
                total.getValue().addAndGet(en.getValue().getValue().get());
            }
            buildLogStr(sb, new AbstractMap.SimpleEntry<>("mapper-total", total), totalTime);
            for (Map.Entry<String, Pair<AtomicInteger, AtomicLong>> en : timeLog.getMapperLogMap().entrySet()) {
                buildLogStr(sb, en, totalTime);
            }
        }
    }

    private void printLog(StringBuilder sb, Throwable te) {
        sb.append("\n--end--\n");
        String msg = sb.toString();
        StaticLog.warn(msg);
        try {
            myMailService.sendErrorMail(te == null ? "运行超时" : "运行超时+异常", msg, te);
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    private void buildLogStr(StringBuilder sb, Map.Entry<String, Pair<AtomicInteger, AtomicLong>> en, long sumTime) {
        sb.append(System.lineSeparator()).append("[")
                .append(sumTime > 0 ? new DecimalFormat("#.##%").format((double)en.getValue().getValue().get() / (double)sumTime) : "100%").append("]");// 百分比
        sb.append("[").append(millisToString(Duration.ofNanos(en.getValue().getValue().longValue()).toMillis())).append("]");// 时间
        sb.append("[").append(en.getValue().getKey()).append("]");// 次数
        sb.append(en.getKey());// 路径
    }

    public static String millisToString(long millis) {
        String[] modUnits = {"分", "秒", "毫秒"};
        long[] mods = {60, 1000, 1};

        if (millis <= 0) {
            return millis + modUnits[modUnits.length - 1];
        }

        long mod = 1;
        for (long t : mods) {
            mod = mod * t;
        }

        long tmpTime = millis;
        StringBuilder timeString = new StringBuilder();
        int bit = 0;
        for (int i = 0; i < modUnits.length && bit <= 2; i++) {
            long curr = tmpTime / mod;
            tmpTime = tmpTime % mod;
            mod = mod / mods[i];
            if (curr > 0) {
                bit++;
                timeString.append(curr).append(modUnits[i]);
            }
            if (bit > 0) {
                bit++;
            }
        }

        return timeString.toString();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MyTimeLog timeLog = MyTimeLog.get();
        String methodPath = String
                .format("%s.%s()", invocation.getMethod().getDeclaringClass().getName(),
                        invocation.getMethod().getName());

        boolean isRoot = false;
        try {
            if (timeLog == null) {
                if (MyTimeLogAdvisor.getTimeoutMillis() < 0) {
                    return invocation.proceed();
                }
                isRoot = true;
                timeLog = MyTimeLog.start();
            }

            Pair<AtomicInteger, AtomicLong> v = timeLog.getLogMap().get(methodPath);
            if (v == null) {
                // 初始化统计信息
                v = Pair.of(new AtomicInteger(), new AtomicLong());
                timeLog.getLogMap().put(methodPath, v);
            }

            // 执行方法
            long totalTime = System.nanoTime();
            Object object = null;
            Throwable te = null;
            try {
                object = invocation.proceed();
            } catch (Throwable e) {
                te = e;
            }
            totalTime = System.nanoTime() - totalTime;

            // 执行次数
            v.getKey().incrementAndGet();
            // 执行时间
            v.getValue().addAndGet(totalTime);

            // toString
            if (isRoot) {
                MyTimeLog myLog = MyTimeLog.end();
                if (TimeUnit.NANOSECONDS.toMillis(totalTime) >= MyTimeLogAdvisor.getTimeoutMillis() && myLog!=null) {
                    StringBuilder sb = new StringBuilder();
                    buildLogStr(sb, myLog, totalTime);
                    printLog(sb, te);
                }
            }

            if (te != null) {
                throw te;
            }
            return object;
        } finally {
            if (isRoot) {
                MyTimeLog.end();
            }
        }
    }
}