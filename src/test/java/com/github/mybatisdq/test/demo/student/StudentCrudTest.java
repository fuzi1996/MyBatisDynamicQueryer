package com.github.mybatisdq.test.demo.student;

import org.apache.ibatis.session.SqlSession;
import com.github.mybatisdq.test.demo.student.entity.Student;
import com.github.mybatisdq.test.demo.student.mapper.StudentMapper;
import com.github.mybatisdq.test.util.SqlSessionUtil;
import org.junit.Test;

import java.util.List;

public class StudentCrudTest {

    private StudentMapper getStudentMapper(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        return sqlSession.getMapper(StudentMapper.class);
    }

    @Test
    public void testInsert(){
        Student student = new Student();
        student.setName("测试数据");
        student.setGender(0);
        student.setGrade(2);
        student.setScore(10);
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        studentMapper.add(student);
        sqlSession.commit();

        System.out.println(String.format("添加后student的id:%s",student.getId()));
    }

    @Test
    public void testDelete(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        studentMapper.deleteById(18L);
        sqlSession.commit();
    }

    @Test
    public void testUpdate(){
        Long id = 1L;
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        Student student = studentMapper.queryById(id);
        System.out.println(String.format("根据id:%s,查到的数据:%s",id,student.toString()));
        student.setScore(student.getScore()+1);
        studentMapper.update(student);
        sqlSession.commit();
        student = studentMapper.queryById(id);
        System.out.println(String.format("根据id:%s,查到的数据:%s",id,student.toString()));
    }

    @Test
    public void testQuery(){
        StudentMapper mapper = getStudentMapper();
        List<Student> students = mapper.queryAll();
        System.out.println(String.format("数据库共有数据:%s条",students.size()));

        Long id = 1L;
        Student student = mapper.queryById(id);
        System.out.println(String.format("根据id:%s,查到的数据:%s",id,student.toString()));
    }

    @Test
    public void testQueryAllUseInclude(){
        StudentMapper studentMapper = getStudentMapper();
        List<Student> students = studentMapper.queryAllUseIncludeTag();
        System.out.println(String.format("数据库共有数据:%s条",students.size()));

        Long id = 1L;
        Student student = studentMapper.queryByIdUseIncludeTag(id);
        System.out.println(String.format("根据id:%s,查到的数据:%s",id,student.toString()));
    }
}
