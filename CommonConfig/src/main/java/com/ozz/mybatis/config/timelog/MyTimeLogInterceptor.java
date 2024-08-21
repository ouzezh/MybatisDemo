package com.ozz.mybatis.config.timelog;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import cn.hutool.log.StaticLog;
import com.ozz.mybatis.service.MyMailService;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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
            for (Map.Entry<String, Tuple> en : timeLog.getLogMap().entrySet()) {
                buildLogStr(sb, en.getKey(), en.getValue().get(0), en.getValue().get(1), totalTime);
            }
        }
        if (!timeLog.getMapperLogMap().isEmpty()) {
            sb.append(System.lineSeparator()).append("--mapper--");
            Pair<AtomicInteger, AtomicLong> mapperTotal = Pair.of(new AtomicInteger(0), new AtomicLong(0));
            for (Map.Entry<String, Pair<AtomicInteger, AtomicLong>> en : timeLog.getMapperLogMap().entrySet()) {
                mapperTotal.getKey().addAndGet(en.getValue().getKey().get());
                mapperTotal.getValue().addAndGet(en.getValue().getValue().get());
            }
            buildLogStr(sb, "mapper-total", mapperTotal.getKey(), mapperTotal.getValue(), totalTime);
            for (Map.Entry<String, Pair<AtomicInteger, AtomicLong>> en : timeLog.getMapperLogMap().entrySet()) {
                buildLogStr(sb, en.getKey(), en.getValue().getKey(), en.getValue().getValue(), totalTime);
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

    private void buildLogStr(StringBuilder sb, String methodPath, AtomicInteger invokeCount, AtomicLong costTime, long totalTime) {
        sb.append(System.lineSeparator()).append("[")
                .append(totalTime > 0 ? new DecimalFormat("#.##%").format((double)costTime.get() / (double)totalTime) : "100%").append("]");// 百分比
        sb.append("[").append(formatBetween(Duration.ofNanos(costTime.longValue()).toMillis())).append("]");// 时间
        sb.append("[").append(invokeCount).append("]");// 次数
        sb.append(methodPath);// 路径
    }

    public static String formatBetween(long millis) {
        return DateUtil.formatBetween(millis);
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

            // 统计信息
            Tuple v = timeLog.getLogMap().computeIfAbsent(methodPath, k -> new Tuple(new AtomicInteger(), new AtomicLong(), new AtomicBoolean(false)));
            AtomicInteger invokeCount = v.get(0);
            AtomicLong costTime = v.get(1);
            AtomicBoolean isRunning = v.get(2);

            boolean isRun = isRunning.get();
            if(!isRun) {
                isRunning.set(true);
            }

            // 执行方法
            long ct = System.nanoTime();
            Object object = null;
            Throwable te = null;
            try {
                object = invocation.proceed();
            } catch (Throwable e) {
                te = e;
            }
            ct = System.nanoTime() - ct;

            // 执行次数
            invokeCount.incrementAndGet();
            // 执行时间
            if(!isRun) {
                costTime.addAndGet(ct);
                isRunning.set(false);
            }

            // toString
            if (isRoot) {
                if (TimeUnit.NANOSECONDS.toMillis(ct) >= MyTimeLogAdvisor.getTimeoutMillis()) {
                    StringBuilder sb = new StringBuilder();
                    buildLogStr(sb, timeLog, ct);
                    printLog(sb, te);
                }
            }

            if (te != null) {
                throw te;
            }
            return object;
        } finally {
            if (isRoot) {
                MyTimeLog.stop();
            }
        }
    }
}
