package com.example.demo.service;

import com.example.demo.model.ShiftTemplate;
import java.util.List;

public interface ShiftTemplateService {
    ShiftTemplate create(ShiftTemplate template);
    List<ShiftTemplate> findByDepartment(Long deptId);
    void delete(Long id);
}