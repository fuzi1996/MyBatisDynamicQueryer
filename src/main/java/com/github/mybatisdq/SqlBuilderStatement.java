package com.github.mybatisdq;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

public class SqlBuilderStatement {

    private Configuration configuration;
    private LanguageDriver languageDriver;

    public SqlBuilderStatement(Configuration configuration) {
        this.configuration = configuration;
        this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
    }

    /**
     * 生成缓存map的缓存key
     * @param sql
     * @param parameterType
     * @param resultType
     * @param sqlCommandType
     * @return
     */
    private String generateCacheKey(String sql, Class<?> parameterType,
                                    Class<?> resultType, SqlCommandType sqlCommandType) {
        return sqlCommandType.toString() + "." + (resultType + sql + parameterType)
                .hashCode();
    }

    /**
     * 判断是否已经缓存
     * @param cacheKey
     * @return
     */
    private boolean hasMappedStatement(String cacheKey) {
        return this.configuration.hasStatement(cacheKey, false);
    }

    /**
     * 当没有缓存MappedStatement时，添加MappedStatement缓存
     * @param cacheKey
     * @param sqlSource
     * @param resultType
     */
    private MappedStatement addNewSelectMappedStatement(String cacheKey,
                                                        SqlSource sqlSource,
                                                        final Class<?> resultType) {
        List<ResultMap> resultMapList = new ArrayList<>();
        resultMapList.add((new ResultMap.Builder(this.configuration,
                "defaultResultMap", resultType, new ArrayList(0))).build());
        MappedStatement mappedStatement = (new MappedStatement.Builder(
                this.configuration,
                cacheKey,
                sqlSource,
                SqlCommandType.SELECT)).resultMaps(resultMapList).build();
        this.configuration.addMappedStatement(mappedStatement);
        return mappedStatement;
    }

    /**
     * 获取缓存key,如果没有缓存则会添加MappedStatement缓存
     * @param sql
     * @param parameterType
     * @param resultType
     * @return
     */
    public String getCacheKeyWithStore(String sql, Class<?> parameterType,
                                        Class<?> resultType) {
        String cacheKey = this
                .generateCacheKey(sql, parameterType, resultType, SqlCommandType.SELECT);
        if (!this.hasMappedStatement(cacheKey)) {
            SqlSource sqlSource = this.languageDriver
                    .createSqlSource(this.configuration, sql, parameterType);
            this.addNewSelectMappedStatement(cacheKey, sqlSource, resultType);
        }
        return cacheKey;
    }

}
