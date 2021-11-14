package com.github.mybatisdq.test.sql.statics;

import com.github.mybatisdq.MyBatisDynamicQueryer;
import com.github.mybatisdq.test.demo.student.entity.Student;
import com.github.mybatisdq.test.demo.student.mapper.StudentMapper;
import com.github.mybatisdq.test.demo.student.support.StudentMap;
import com.github.mybatisdq.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class StaticScriptTest {

    @Test
    public void testScriptTagList() {
        String sqlStr = "<script>SELECT * FROM `students`</script>";
        String sqlStr1 = "SELECT * FROM `students`</script>";
        String sqlStr2 = "<script>SELECT * FROM `students`";
        String sqlStrWithoutScriptTab = "SELECT * FROM `students`";
        MyBatisDynamicQueryer queryer = TestUtil.getMyBatisDynamicQueryer();
        List<Map> maps = queryer.selectList(sqlStr, null, Map.class);
        List<Map> maps1 = queryer.selectList(sqlStr1, null, Map.class);
        List<Map> maps2 = queryer.selectList(sqlStr2, null, Map.class);
        List<Map> maps3 = queryer.selectList(sqlStrWithoutScriptTab, null, Map.class);


        StudentMapper studentMapper = TestUtil.getStudentMapper();
        List<Student> students = studentMapper.queryAll();

        Assert.assertEquals(students.size(),maps.size());
        Assert.assertEquals(students.size(),maps1.size());
        Assert.assertEquals(students.size(),maps2.size());
        Assert.assertEquals(students.size(),maps3.size());

    }

    @Test
    public void testStaticSelect(){
        StudentMapper studentMapper = TestUtil.getStudentMapper();
        List<Student> students = studentMapper.queryAll();
        Student student = students.stream().findAny().get();

        String sqlStr = "select * from students where id = #{id}";
        MyBatisDynamicQueryer queryer = TestUtil.getMyBatisDynamicQueryer();
        List<Map> maps = queryer.selectList(sqlStr, student.getId(), Map.class);

        Assert.assertEquals(1,maps.size());

        StudentMap stuMap = new StudentMap(maps.get(0));
        Assert.assertEquals(student.getId(),stuMap.getId());
    }
}
