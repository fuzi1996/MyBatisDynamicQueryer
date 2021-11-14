package com.github.mybatisdq.test.demo.student.mapper;


import com.github.mybatisdq.test.demo.student.entity.Student;

import java.util.List;

public interface StudentMapper {
    void add(Student student);

    void deleteById(Long id);

    void update(Student student);

    Student queryById(Long id);

    List<Student> findByNameLike(String name);

    List<Student> queryAll();

    Student queryByIdUseIncludeTag(Long id);

    List<Student> queryAllUseIncludeTag();

}
