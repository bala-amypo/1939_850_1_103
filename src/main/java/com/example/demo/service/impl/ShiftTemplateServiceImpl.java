package com.example.demo.service.impl;

import com.example.demo.model.ShiftTemplate;
import com.example.demo.repository.ShiftTemplateRepository;
import com.example.demo.service.ShiftTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ShiftTemplateServiceImpl implements ShiftTemplateService {
    private final ShiftTemplateRepository shiftTemplateRepository;

    public ShiftTemplateServiceImpl(ShiftTemplateRepository shiftTemplateRepository) {
        this.shiftTemplateRepository = shiftTemplateRepository;
    }

    @Override
    public ShiftTemplate create(ShiftTemplate template) {
        if (template.getEndTime().isBefore(template.getStartTime()) || 
            template.getEndTime().equals(template.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        
        ShiftTemplate existing = shiftTemplateRepository.findByTemplateNameAndDepartment_Id(
            template.getTemplateName(), template.getDepartment().getId());
        if (existing != null) {
            throw new IllegalArgumentException("Template name must be unique within department");
        }
        
        return shiftTemplateRepository.save(template);
    }

    @Override
    public List<ShiftTemplate> getByDepartment(Long departmentId) {
        return shiftTemplateRepository.findByDepartment_Id(departmentId);
    }
}