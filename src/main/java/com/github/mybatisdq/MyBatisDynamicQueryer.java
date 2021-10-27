package com.github.mybatisdq;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class MyBatisDynamicQueryer {

    private SqlSessionFactory sqlSessionFactory;

    private SqlBuilderStatement sqlBuilderStatement;

    public MyBatisDynamicQueryer(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlBuilderStatement = new SqlBuilderStatement(sqlSessionFactory.getConfiguration());
    }

    public <T> List<T> selectList(String sqlScript, Object parameterObject, Class<T> resultType) {
        SqlSession sqlSession = null;
        Class<?> parameterType = parameterObject != null ? parameterObject.getClass() : null;
        String cacheKey = "";
        try {
            cacheKey = this.sqlBuilderStatement
                    .getCacheKeyWithStore(sqlScript, parameterType, resultType);
            sqlSession = sqlSessionFactory.openSession(true);
            return sqlSession.selectList(cacheKey, parameterObject);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
