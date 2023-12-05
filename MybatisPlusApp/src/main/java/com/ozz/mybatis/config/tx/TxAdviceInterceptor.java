package com.ozz.mybatis.config.tx;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.Properties;

/**
 * 自动开启事务配置
 */
//@Configuration
public class TxAdviceInterceptor {
  private static final int TX_METHOD_TIMEOUT = 300;
  private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.ozz.mybatis.service..*.*(..))";

  @Bean
  public TransactionInterceptor txInterceptor(PlatformTransactionManager transactionManager) {
    Properties transactionAttributes = new Properties();

    RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
    requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
    requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    requiredTx.setTimeout(TX_METHOD_TIMEOUT);// 超时时间(s)
    String requiredTxDef = requiredTx.toString();

    transactionAttributes.put("add*", requiredTxDef);
    transactionAttributes.put("save*", requiredTxDef);
    transactionAttributes.put("insert*", requiredTxDef);
    transactionAttributes.put("update*", requiredTxDef);
    transactionAttributes.put("delete*", requiredTxDef);

    TransactionInterceptor txAdvice = new TransactionInterceptor();
    txAdvice.setTransactionAttributes(transactionAttributes);
    txAdvice.setTransactionManager(transactionManager);
    return txAdvice;
  }

  @Bean
  public DefaultPointcutAdvisor txPointcutAdvisor(TransactionInterceptor txInterceptor) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
    return new DefaultPointcutAdvisor(pointcut, txInterceptor);
  }
}
