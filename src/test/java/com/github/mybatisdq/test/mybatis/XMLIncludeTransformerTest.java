package com.github.mybatisdq.test.mybatis;

import com.github.mybatisdq.test.util.SqlSessionFactoryUtil;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

public class XMLIncludeTransformerTest {
    @Test
    public void transformerTest(){
        String sql = "<script>\n" +
                "        select\n" +
                "        <include refid=\"com.github.mybatisdq.test.demo.student.mapper.StudentMapper.selectColumns\">\n" +
                "            <property name=\"alias\" value=\"t1\"/>\n" +
                "        </include>\n" +
                "        from students t1\n" +
                "        where id = #{id}\n" +
                "    </script>";
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
        Configuration configuration = sqlSessionFactory.getConfiguration();
        MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration,"");
        XPathParser xPathParser = new XPathParser(sql);
        XNode context = xPathParser.evalNode("//script");
        XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
        includeParser.applyIncludes(context.getNode());
        String textContent = context.getNode().getOwnerDocument().getTextContent();
        System.out.println(textContent);
    }
}
