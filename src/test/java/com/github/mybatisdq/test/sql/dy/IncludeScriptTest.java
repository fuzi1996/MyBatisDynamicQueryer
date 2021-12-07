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

public class IncludeScriptTest {

    private String sqlStr = "select\n" +
            "        <include refid=\"com.github.mybatisdq.test.demo.student.mapper.StudentMapper.selectColumns\">\n" +
            "            <property name=\"alias\" value=\"t1\"/>\n" +
            "        </include>\n" +
            "        from students t1\n" +
            "        where id = #{id}";

    @Test
    public void includeTest(){
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

    /**
     * sqlProvider方式参数有问题
     */
    @Test
    public void includeTestBySqlProvider(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        GlobalConfigurationCache.setConfiguration(sqlSession.getConfiguration());

        Long id = 1L;
        StudentMapper studentMapper = TestUtil.getStudentMapper();
        Student student = studentMapper.queryByIdUseIncludeTag(id);

        StudentMap stuMap = new StudentMap(new LinkedHashMap());
        stuMap.setId(id);

        Map<String, Object> realMap = stuMap.getRealMap();

        realMap.put(DynamicSelectConstant.getDefaultSqlValueKey(),sqlStr);
        List<Map<String,Object>> maps = studentMapper.queryBySqlProvider(realMap);

        Assert.assertNotNull(maps);
        Assert.assertEquals(1,maps.size());
        Assert.assertEquals(student.getName(),new StudentMap(maps.get(0)).getName());
    }
}
