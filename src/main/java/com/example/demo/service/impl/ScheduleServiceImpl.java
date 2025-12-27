package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    private final ShiftTemplateRepository stRepo;
    private final AvailabilityRepository avRepo;
    private final DepartmentRepository deptRepo;
    private final GeneratedShiftScheduleRepository schRepo;
    private final EmployeeRepository empRepo; // Added to satisfy Test Suite requirement

    /**
     * FIX: Updated constructor to accept 5 arguments as required by MasterTestNGSuiteTest:45.
     * The test suite explicitly passes: 
     * (ShiftTemplateRepo, AvailabilityRepo, DepartmentRepo, EmployeeRepo, GeneratedShiftScheduleRepo)
     */
    public ScheduleServiceImpl(ShiftTemplateRepository st, 
                               AvailabilityRepository av, 
                               DepartmentRepository d, 
                               EmployeeRepository e, 
                               GeneratedShiftScheduleRepository s) {
        this.stRepo = st; 
        this.avRepo = av; 
        this.deptRepo = d; 
        this.empRepo = e;
        this.schRepo = s;
    }

    @Override
    public List<GeneratedShiftSchedule> generateForDate(LocalDate date) {
        // Clear existing schedules for the date to prevent duplicates if tests re-run
        List<GeneratedShiftSchedule> existing = schRepo.findByShiftDate(date);
        schRepo.deleteAll(existing);

        List<GeneratedShiftSchedule> results = new ArrayList<>();
        List<Department> depts = deptRepo.findAll();
        
        // Fetch only employees explicitly marked as 'Available' for this date
        List<EmployeeAvailability> availableStaff = avRepo.findByAvailableDateAndAvailable(date, true);

        for (Department dept : depts) {
            // Find templates specific to this department
            List<ShiftTemplate> templates = stRepo.findByDepartment_Id(dept.getId());
            
            for (ShiftTemplate st : templates) {
                for (EmployeeAvailability av : availableStaff) {
                    Employee emp = av.getEmployee();
                    
                    // Skill matching: Employee must possess the skills required by the template
                    if (emp.getSkills() != null && st.getRequiredSkills() != null && 
                        emp.getSkills().contains(st.getRequiredSkills())) {
                        
                        GeneratedShiftSchedule sch = new GeneratedShiftSchedule();
                        sch.setEmployee(emp);
                        sch.setDepartment(dept);
                        sch.setShiftDate(date);
                        
                        // Persistent save as required by SRS referential integrity
                        results.add(schRepo.save(sch));
                    }
                }
            }
        }
        return results;
    }

    @Override
    public List<GeneratedShiftSchedule> getByDate(LocalDate date) {
        // Test priority 62 (testScheduleDateParse) relies on this returning a list
        return schRepo.findByShiftDate(date);
    }
}