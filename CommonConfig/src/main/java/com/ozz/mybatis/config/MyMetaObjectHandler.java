package com.ozz.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis plus 自动填写公共属性
 * 需配置注解 @TableField.fill
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        fillDatetime(metaObject, "createTime");
        fillDatetime(metaObject, "updateTime");
        if (metaObject.hasSetter("updater")) {
            Object creator = metaObject.getValue("updater");
            if (null != creator) {
                this.setFieldValByName("creator", creator, metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        fillDatetime(metaObject, "updateTime");
    }

    private void fillDatetime(MetaObject metaObject, String updateTime) {
        if (metaObject.hasSetter(updateTime)) {
            this.setFieldValByName(updateTime, LocalDateTime.now(), metaObject);
        }
    }
}
