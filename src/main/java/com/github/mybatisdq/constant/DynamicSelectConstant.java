package com.github.mybatisdq.constant;

public class DynamicSelectConstant {

    private static String DEFAULT_SQL_VALUE_KEY = "$SQL";

    public static String getDefaultSqlValueKey(){
        return DEFAULT_SQL_VALUE_KEY;
    }

    public static void setDefaultSqlValueKey(String key){
        DEFAULT_SQL_VALUE_KEY = key;
    }

}
