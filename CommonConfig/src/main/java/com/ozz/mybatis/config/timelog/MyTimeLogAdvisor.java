package com.ozz.mybatis.config.timelog;

import com.ozz.mybatis.service.MyMailService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 静态AOP配置
 */
@Component
@Aspect
public class MyTimeLogAdvisor extends StaticMethodMatcherPointcutAdvisor {
    private static final long TIMEOUT_MILLIS = 0L;
    private static final String TIMEOUT_PACKAGE = "com.ozz";

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return targetClass.getName().startsWith(TIMEOUT_PACKAGE) && !targetClass.getName().startsWith(this.getClass().getPackage().getName());
    }

    public MyTimeLogAdvisor() {
        super.setAdvice(new MyTimeLogInterceptor());
    }

    @Autowired
    public void setMyMailService(MyMailService myMailService) {
        ((MyTimeLogInterceptor) getAdvice()).setMyMailService(myMailService);
    }

    /**
     * 超时时间，可以针对特定请求修改限时设置
     *
     * @return 超时时间设置
     */
    public static long getTimeoutMillis() {
        return MyTimeLogAdvisor.TIMEOUT_MILLIS;
    }
}
