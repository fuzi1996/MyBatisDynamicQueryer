package com.github.mybatisdq;

import com.github.mybatisdq.test.util.SqlSessionFactoryUtil;
import junit.framework.TestCase;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class MyBatisDynamicQueryerTest extends TestCase {

    public void testGetSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

        SqlSession sqlSession = sqlSessionFactory.openSession();

        Configuration configuration = sqlSession.getConfiguration();

        Connection connection = sqlSession.getConnection();
    }

    public void testSelectList() {
        String sqlStr = "<script>SELECT * FROM `students`</script>";
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
        MyBatisDynamicQueryer queryer = new MyBatisDynamicQueryer(sqlSessionFactory);
        List<Map> maps = queryer.selectList(sqlStr, null, Map.class);
        System.out.println(maps.size());
    }


}