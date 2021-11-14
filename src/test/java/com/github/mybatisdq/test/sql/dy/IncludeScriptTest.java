package com.github.mybatisdq.test.sql.dy;

import com.github.mybatisdq.MyBatisDynamicQueryer;
import com.github.mybatisdq.test.demo.student.entity.Student;
import com.github.mybatisdq.test.demo.student.mapper.StudentMapper;
import com.github.mybatisdq.test.demo.student.support.StudentMap;
import com.github.mybatisdq.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IncludeScriptTest {
    @Test
    public void ifTest(){
        String sqlStr = "select\n" +
                "        <include refid=\"com.github.mybatisdq.test.demo.student.mapper.StudentMapper.selectColumns\">\n" +
                "            <property name=\"alias\" value=\"t1\"/>\n" +
                "        </include>\n" +
                "        from students t1\n" +
                "        where id = #{id}";
        Long id = 1L;
        StudentMapper studentMapper = TestUtil.getStudentMapper();
        Student student = studentMapper.queryByIdUseIncludeTag(id);


        StudentMap stuMap = new StudentMap(new LinkedHashMap());
        stuMap.setId(id);


        MyBatisDynamicQueryer queryer = TestUtil.getMyBatisDynamicQueryer();
        List<Map> maps = queryer.selectList(sqlStr, stuMap, Map.class);

        Assert.assertNotNull(maps);
        Assert.assertEquals(1,maps.size());
        Assert.assertEquals(student.getName(),new StudentMap(maps.get(0)).getName());
    }
}
