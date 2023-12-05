package com.ozz.mybatis.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MybatisPlusTableMapExecutor {
    public static final ThreadLocal<CaseInsensitiveMap<String, String>> TABLE_MAP = ThreadLocal.withInitial(CaseInsensitiveMap::new);

    public static void clear() {
        TABLE_MAP.get().clear();
    }

    public static void execute(CaseInsensitiveMap<String, String> tableMap, Runnable runnable) {
        Set<String> keys;
        if(TABLE_MAP.get() == null) {
            keys = tableMap.keySet();
            TABLE_MAP.set(tableMap);
        } else {
            keys = new HashSet<>();
            for (Map.Entry<String, String> en : tableMap.entrySet()) {
                String tmp = TABLE_MAP.get().get(en.getKey());
                if(tmp != null) {
                    if(!tmp.equalsIgnoreCase(en.getValue())) {
                        ExceptionUtil.wrapRuntimeAndThrow(StrUtil.format("dynamic table name conflict, table: {}, toTable: {} {}", en.getKey(), en.getValue(), tmp));
                    }
                } else {
                    keys.add(en.getKey());
                    TABLE_MAP.get().put(en.getKey(), en.getValue());
                }
            }
        }
        try {
            runnable.run();
        } finally {
            keys.stream().forEach(TABLE_MAP.get()::remove);
        }
    }
}
