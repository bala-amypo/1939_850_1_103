package com.example.demo.service.impl;

import com.example.demo.model.ShiftTemplate;
import com.example.demo.repository.ShiftTemplateRepository;
import com.example.demo.repository.DepartmentRepository; // Required dependency
import com.example.demo.service.ShiftTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ShiftTemplateServiceImpl implements ShiftTemplateService {
    private final ShiftTemplateRepository shiftTemplateRepository;
    private final DepartmentRepository departmentRepository; // Added for Test compatibility

    // FIX: Updated constructor to accept 2 arguments as required by MasterTestNGSuiteTest:43
    public ShiftTemplateServiceImpl(ShiftTemplateRepository shiftTemplateRepository, 
                                    DepartmentRepository departmentRepository) {
        this.shiftTemplateRepository = shiftTemplateRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public ShiftTemplate create(ShiftTemplate template) {
        // Test suite requirement: Message must contain "after"
        if (template.getEndTime().isBefore(template.getStartTime()) || 
            template.getEndTime().equals(template.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }
        
        // Uniqueness check for template name within a department
        shiftTemplateRepository.findByTemplateNameAndDepartment_Id(
            template.getTemplateName(), 
            template.getDepartment().getId()
        ).ifPresent(t -> {
            throw new RuntimeException("Template name must be unique within department");
        });
        
        return shiftTemplateRepository.save(template);
    }

    // FIX: Added 'getByDepartment' because MasterTestNGSuiteTest:311 cannot find this symbol
    @Override
    public List<ShiftTemplate> getByDepartment(Long departmentId) {
        return findByDepartment(departmentId);
    }

    @Override
    public List<ShiftTemplate> findByDepartment(Long departmentId) {
        return shiftTemplateRepository.findByDepartment_Id(departmentId);
    }

    @Override
    public void delete(Long id) {
        ShiftTemplate st = shiftTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        shiftTemplateRepository.delete(st);
    }
}