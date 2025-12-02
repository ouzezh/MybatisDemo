package com.ozz.mybatis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "my.datasource.dynamic")
public class MyDynamicDBProperties {
    private String primary;
    private Map<String, Map<String, String>> dataSource = new LinkedHashMap<>();
}
