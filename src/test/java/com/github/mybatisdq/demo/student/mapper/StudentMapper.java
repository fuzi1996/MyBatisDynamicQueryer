package com.github.mybatisdq.demo.student.mapper;
import com.github.mybatisdq.demo.student.entity.Student;

import java.util.List;

public interface StudentMapper {
    void add(Student student);

    void deleteById(Long id);

    void update(Student student);

    Student queryById(Long id);

    List<Student> queryAll();

}
