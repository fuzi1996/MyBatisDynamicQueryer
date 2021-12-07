package com.github.mybatisdq.cache;

import java.util.HashMap;
import java.util.Map;

public class IncludeSqlCache {
    private static final Map<String,String> INCLUDE_SQL_CACHE = new HashMap<>();

    public static String getCache(String sql){
        return INCLUDE_SQL_CACHE.get(sql);
    }

    public static void putCache(String sqlKey,String sqlValue){
        INCLUDE_SQL_CACHE.put(sqlKey,sqlValue);
    }

}
