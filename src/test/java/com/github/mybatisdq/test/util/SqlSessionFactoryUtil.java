package com.github.mybatisdq.test.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryUtil {

    public static SqlSessionFactory getSqlSessionFactory(){
        String resource = "mybatis-config.xml";
        try(InputStream inputStream = Resources.getResourceAsStream(resource)) {
            return new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
