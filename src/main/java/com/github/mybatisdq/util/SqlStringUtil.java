package com.github.mybatisdq.util;

public class SqlStringUtil {

    private static final String SCRIPT_PREFIX = "<script>";
    private static final String SCRIPT_SUFFIX = "</script>";

    private SqlStringUtil(){}

    public static String appendScriptTag(String sql){
        StringBuffer sqlBuff = new StringBuffer();
        if(!sql.startsWith(SCRIPT_PREFIX)){
            sqlBuff.append(SCRIPT_PREFIX);
        }
        sqlBuff.append(sql);
        if(!sql.endsWith(SCRIPT_SUFFIX)){
            sqlBuff.append(SCRIPT_SUFFIX);
        }
        return sqlBuff.toString();
    }
}
