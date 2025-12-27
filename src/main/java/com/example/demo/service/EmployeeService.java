package com.example.demo.service;

import com.example.demo.model.Employee;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee getEmployee(Long id);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
    Employee findByEmail(String email);
    List<Employee> getAll();
}
