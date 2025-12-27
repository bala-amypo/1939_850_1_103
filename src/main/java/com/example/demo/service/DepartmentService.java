package com.example.demo.service;

import com.example.demo.model.Department;
import java.util.List;

public interface DepartmentService {
    Department create(Department department);
    Department getById(Long id);
    List<Department> getAll();
    void delete(Long id);
}