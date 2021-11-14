package com.github.mybatisdq.test.util;

import com.github.mybatisdq.MyBatisDynamicQueryer;
import com.github.mybatisdq.test.demo.student.mapper.StudentMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class TestUtil {
    public static StudentMapper getStudentMapper(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        return sqlSession.getMapper(StudentMapper.class);
    }

    public static MyBatisDynamicQueryer getMyBatisDynamicQueryer(){
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
        return new MyBatisDynamicQueryer(sqlSessionFactory);
    }
}
