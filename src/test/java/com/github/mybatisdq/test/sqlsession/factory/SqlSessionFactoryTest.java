package com.github.mybatisdq.test.sqlsession.factory;

import com.github.mybatisdq.test.util.SqlSessionUtil;
import org.junit.Test;

import java.io.IOException;

public class SqlSessionFactoryTest {
    @Test
    public void testBuildSqlSessionFactory() throws IOException {
        SqlSessionUtil.getSqlSessionFactory();
        System.out.println("SqlSessionFactory build success!");
    }
}
