package com.example.demo.service.impl;

import com.example.demo.model.ShiftTemplate;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.ShiftTemplateRepository;
import com.example.demo.service.ShiftTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShiftTemplateServiceImpl implements ShiftTemplateService {

    private final ShiftTemplateRepository shiftTemplateRepository;
    private final DepartmentRepository departmentRepository;

    public ShiftTemplateServiceImpl(ShiftTemplateRepository shiftTemplateRepository,
                                   DepartmentRepository departmentRepository) {
        this.shiftTemplateRepository = shiftTemplateRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public ShiftTemplate create(ShiftTemplate shiftTemplate) {
        if (shiftTemplate.getDepartment() != null && shiftTemplate.getDepartment().getId() != null) {
            departmentRepository.findById(shiftTemplate.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
        }
        
        if (shiftTemplate.getEndTime().isBefore(shiftTemplate.getStartTime()) ||
            shiftTemplate.getEndTime().equals(shiftTemplate.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        
        if (shiftTemplate.getDepartment() != null && shiftTemplate.getDepartment().getId() != null) {
            Optional<ShiftTemplate> existing = shiftTemplateRepository.findByTemplateNameAndDepartment_Id(
                    shiftTemplate.getTemplateName(), 
                    shiftTemplate.getDepartment().getId()
            );
            
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Shift template name must be unique within department");
            }
        }
        
        return shiftTemplateRepository.save(shiftTemplate);
    }

    @Override
    public List<ShiftTemplate> getByDepartment(Long departmentId) {
        return shiftTemplateRepository.findByDepartment_Id(departmentId);
    }

    @Override
    public List<ShiftTemplate> getAll() {
        return shiftTemplateRepository.findAll();
    }
}