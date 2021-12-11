package com.github.mybatisdq.cache;

import org.apache.ibatis.session.Configuration;

public class GlobalConfigurationCache {
    private static Configuration configuration;

    public static void setConfiguration(Configuration configuration){
        GlobalConfigurationCache.configuration = configuration;
    }

    public static Configuration getConfiguration(){
        if(null == configuration){
            throw new IllegalArgumentException("un init configuration");
        }
        return configuration;
    }
}
