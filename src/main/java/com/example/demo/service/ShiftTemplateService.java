package com.example.demo.service;

import com.example.demo.model.ShiftTemplate;
import java.util.List;

public interface ShiftTemplateService {
    
    ShiftTemplate create(ShiftTemplate template);
    
    // FIX: Added to satisfy the @Override in ShiftTemplateServiceImpl
    // and the specific call from MasterTestNGSuiteTest:311
    List<ShiftTemplate> getByDepartment(Long departmentId);

    List<ShiftTemplate> findByDepartment(Long deptId);
    
    void delete(Long id);
}