package com.github.mybatisdq.test.mybatis;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;

public class XmlMapperBuilderTest {

    @Test
    public void shouldSuccessfullyLoadXMLMapperFile() throws Exception {
        Configuration configuration = new Configuration();
        String resource = "com/github/mybatisdq/test/demo/student/mapper/includeScriptMapper.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            builder.parse();
        }
    }
}
