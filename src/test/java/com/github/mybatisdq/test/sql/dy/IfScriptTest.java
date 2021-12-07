package com.github.mybatisdq.test.sql.dy;

import com.github.mybatisdq.MyBatisDynamicQueryer;
import com.github.mybatisdq.cache.GlobalConfigurationCache;
import com.github.mybatisdq.constant.DynamicSelectConstant;
import com.github.mybatisdq.test.demo.student.entity.Student;
import com.github.mybatisdq.test.demo.student.mapper.StudentMapper;
import com.github.mybatisdq.test.demo.student.support.StudentMap;
import com.github.mybatisdq.test.util.SqlSessionUtil;
import com.github.mybatisdq.test.util.TestUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IfScriptTest {

    private String sqlStr = "select * from students\n" +
            "        <bind name=\"namePattern\" value=\"'%' + name + '%'\" />\n" +
            "        <where>\n" +
            "            <if test=\"name != null\">\n" +
            "                and name like #{namePattern}\n" +
            "            </if>\n" +
            "            <if test=\"name == null\">\n" +
            "                and 1 = -1\n" +
            "            </if>\n" +
            "        </where>";

    @Test
    public void ifTest(){
        String selectKeyWords = "林";
        StudentMapper studentMapper = TestUtil.getStudentMapper();
        List<Student> students = studentMapper.findByNameLike(selectKeyWords);

        StudentMap stuMap = new StudentMap(new LinkedHashMap());
        stuMap.setName(selectKeyWords);

        MyBatisDynamicQueryer queryer = TestUtil.getMyBatisDynamicQueryer();
        List<Map> maps = queryer.selectList(sqlStr, stuMap, Map.class);

        Assert.assertNotNull(maps);
        Assert.assertEquals(students.size(),maps.size());
    }

    /**
     * sqlProvider方式参数有问题
     */
    @Test
    public void ifTestBySqlProvider(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        GlobalConfigurationCache.setConfiguration(sqlSession.getConfiguration());

        String selectKeyWords = "小";
        StudentMapper studentMapper = TestUtil.getStudentMapper();
        List<Student> students = studentMapper.findByNameLike(selectKeyWords);

        LinkedHashMap realMap = new LinkedHashMap();
        StudentMap stuMap = new StudentMap(realMap);
        stuMap.setName(selectKeyWords);

        realMap.put(DynamicSelectConstant.getDefaultSqlValueKey(),sqlStr);
        List<Map<String, Object>> maps = studentMapper.queryBySqlProvider(realMap);

        Assert.assertNotNull(maps);
        Assert.assertEquals(students.size(),maps.size());
    }
}
