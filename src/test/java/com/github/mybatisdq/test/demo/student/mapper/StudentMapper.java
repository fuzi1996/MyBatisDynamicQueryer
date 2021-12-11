package com.github.mybatisdq.test.demo.student.mapper;


import com.github.mybatisdq.DynamicSelectSqlProvider;
import com.github.mybatisdq.test.demo.student.entity.Student;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface StudentMapper {
    void add(Student student);

    void deleteById(Long id);

    void update(Student student);

    Student queryById(Long id);

    List<Student> findByNameLike(String name);

    List<Student> queryAll();

    Student queryByIdUseIncludeTag(Long id);

    List<Student> queryAllUseIncludeTag();

    @SelectProvider(type = DynamicSelectSqlProvider.class,method = "getDynamicSelectSql")
    List<Map<String,Object>> queryBySqlProvider(Map<String,Object> param);

}
