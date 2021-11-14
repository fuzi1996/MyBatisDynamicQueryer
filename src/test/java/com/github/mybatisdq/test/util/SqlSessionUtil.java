package com.github.mybatisdq.test.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {
    private static SqlSessionFactory sqlSessionFactory;
    private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

    private SqlSessionUtil(){

    }

    static {
        String resource = "mybatis-config.xml";
        try(InputStream inputStream = Resources.getResourceAsStream(resource)) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * 获取SqlSession
     */
    public static SqlSession getSqlSession(){
        //从当前线程中获取SqlSession对象
        SqlSession sqlSession = threadLocal.get();
        //如果SqlSession对象为空
        if(sqlSession == null){
            //在SqlSessionFactory非空的情况下，获取SqlSession对象
            sqlSession = sqlSessionFactory.openSession();
            //将SqlSession对象与当前线程绑定在一起
            threadLocal.set(sqlSession);
        }
        //返回SqlSession对象
        return sqlSession;
    }
    /**
     * 关闭SqlSession与当前线程分开
     */
    public static void closeSqlSession(){
        //从当前线程中获取SqlSession对象
        SqlSession sqlSession = threadLocal.get();
        //如果SqlSession对象非空
        if(sqlSession != null){
            //关闭SqlSession对象
            sqlSession.close();
            //分开当前线程与SqlSession对象的关系，目的是让GC尽早回收
            threadLocal.remove();
        }
    }
}
