package com.example.demo.service;

import com.example.demo.model.ShiftTemplate;
import java.util.List;

public interface ShiftTemplateService {
    ShiftTemplate create(ShiftTemplate template);
    List<ShiftTemplate> getByDepartment(Long departmentId);
}