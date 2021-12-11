package com.github.mybatisdq;

import com.github.mybatisdq.cache.GlobalConfigurationCache;
import com.github.mybatisdq.cache.IncludeSqlCache;
import com.github.mybatisdq.constant.DynamicSelectConstant;
import com.github.mybatisdq.util.SqlStringUtil;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class DynamicSelectSqlProvider {
    public String getDynamicSelectSql(Map<String,Object> param) throws TransformerException {
        Object sqlObj = param.get(DynamicSelectConstant.getDefaultSqlValueKey());
        if(!(sqlObj instanceof String)){
            throw new IllegalArgumentException(String.format("map[%s] must be a String",DynamicSelectConstant.getDefaultSqlValueKey()));
        }
        String sql = SqlStringUtil.appendScriptTag(((String) sqlObj));
        String cacheSqlValue = IncludeSqlCache.getCache(sql);
        if(null == cacheSqlValue || cacheSqlValue.isEmpty()){
            Configuration configuration = GlobalConfigurationCache.getConfiguration();
            MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration,"");

            XPathParser xPathParser = new XPathParser(sql);
            XNode node = xPathParser.evalNode("/script");

            XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
            includeParser.applyIncludes(node.getNode());

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            transformer.setOutputProperty("omit-xml-declaration","yes");
            transformer.transform(new DOMSource(node.getNode()),new StreamResult(out));
            String sqlValue = out.toString();
            IncludeSqlCache.putCache(sql,sqlValue);
            return sqlValue;
        }else {
            return cacheSqlValue;
        }
    }
}
