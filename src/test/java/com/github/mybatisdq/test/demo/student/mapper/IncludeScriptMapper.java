package com.github.mybatisdq.test.demo.student.mapper;

import com.github.mybatisdq.test.demo.student.entity.Student;

public interface IncludeScriptMapper {
    Student queryByIdUseIncludeTag(Long id);
}
