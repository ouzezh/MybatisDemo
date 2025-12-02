package com.ozz.mybatis.config;

import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 集成ShardingSphere读写分离
 */
@EnableConfigurationProperties(MyDynamicDBProperties.class)
@Component
public class MyDynamicDBConfig implements InitializingBean {
    @Resource
    private DynamicRoutingDataSource dynamicRoutingDataSource;
    @Resource
    private MyDynamicDBProperties myDynamicDBProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
//        for (Map.Entry<String, Map<String, String>> en : myDynamicDBProperties.getDataSource().entrySet()) {
//            String dataSourceName = en.getKey();
//            String masterDb = en.getValue().get("masterDatasourceName");
//            List<String> slaveDbs = Arrays.stream(en.getValue().get("slaveDatasourceName").split(",")).map(String::trim).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
//            Assert.notEmpty(masterDb, "主库未指定:"+dataSourceName);
//            Assert.notEmpty(slaveDbs, "从库未指定:"+dataSourceName);
//            Assert.isTrue(!dynamicRoutingDataSource.getDataSources().containsKey(dataSourceName), "数据库配置重复:"+masterDb);
//
//            Map<String, DataSource> dataSourceMap = new HashMap<>();
//            DataSource tmp = getDataSource(masterDb);
//            dataSourceMap.put(masterDb, tmp);
//            for (String slaveDb : slaveDbs) {
//                tmp = getDataSource(slaveDb);
//                dataSourceMap.put(slaveDb, tmp);
//            }
//
//            ReadwriteSplittingDataSourceRuleConfiguration dataSourceConfig = new ReadwriteSplittingDataSourceRuleConfiguration(
//                    dataSourceName, new StaticReadwriteSplittingStrategyConfiguration(masterDb, slaveDbs), null, "round_robin_lb");
//            Map<String, AlgorithmConfiguration> algorithmConfigMap = new HashMap<>(1);
//            algorithmConfigMap.put("round_robin_lb", new AlgorithmConfiguration("ROUND_ROBIN", new Properties()));
//            ReadwriteSplittingRuleConfiguration ruleConfig = new ReadwriteSplittingRuleConfiguration(Collections.singleton(dataSourceConfig), algorithmConfigMap);
//            Properties props = new Properties();
//            props.setProperty("sql-show", Boolean.FALSE.toString());
//            DataSource ds = ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(ruleConfig), props);
//            dynamicRoutingDataSource.getDataSources().put(dataSourceName, ds);
//
//        }
//
//        dynamicRoutingDataSource.setPrimary(myDynamicDBProperties.getPrimary());
    }

    private DataSource getDataSource(String dbName) {
        DataSource tmp = dynamicRoutingDataSource.getDataSources().get(dbName);
        Assert.notNull(tmp, "数据库配置不存在:" + dbName);
        if(tmp instanceof ItemDataSource) {
            return ((ItemDataSource) tmp).getRealDataSource();
        }
        return tmp;
    }

}
