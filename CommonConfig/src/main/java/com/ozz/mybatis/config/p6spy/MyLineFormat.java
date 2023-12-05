package com.ozz.mybatis.config.p6spy;

import com.p6spy.engine.spy.appender.CustomLineFormat;

public class MyLineFormat extends CustomLineFormat {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = sql.replaceAll("(\\s*\n){2,}", "\n");
        return super.formatMessage(connectionId, now, elapsed, category, prepared, sql, url);
    }
}
