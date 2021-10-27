package com.github.mybatisdq.test.connection;

import com.github.mybatisdq.demo.student.constant.DBConstant;
import com.github.mybatisdq.test.util.SqlSessionUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {
    @Test
    public void testConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBConstant.JDBC_URL, DBConstant.JDBC_USER, DBConstant.JDBC_PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(null != connection){
                connection.close();
            }
        }
    }

    @Test
    public void testConnectionBySqlSession(){
        Connection conn = SqlSessionUtil.getSqlSession().getConnection();
        System.out.println(conn!=null?"连接成功":"连接失败");
    }
}
