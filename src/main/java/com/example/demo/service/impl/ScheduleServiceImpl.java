package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ScheduleService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ShiftTemplateRepository shiftTemplateRepository;
    private final AvailabilityRepository availabilityRepository;
    private final EmployeeRepository employeeRepository;
    private final GeneratedShiftScheduleRepository scheduleRepository;
    private final DepartmentRepository departmentRepository;

    public ScheduleServiceImpl(ShiftTemplateRepository shiftTemplateRepository,
                              AvailabilityRepository availabilityRepository,
                              EmployeeRepository employeeRepository,
                              GeneratedShiftScheduleRepository scheduleRepository,
                              DepartmentRepository departmentRepository) {
        this.shiftTemplateRepository = shiftTemplateRepository;
        this.availabilityRepository = availabilityRepository;
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<GeneratedShiftSchedule> generateForDate(LocalDate date) {
        List<GeneratedShiftSchedule> schedules = new ArrayList<>();
        
        List<Department> departments = departmentRepository.findAll();
        List<EmployeeAvailability> availabilities = availabilityRepository.findByAvailableDateAndAvailable(date, true);
        
        for (Department department : departments) {
            List<ShiftTemplate> templates = shiftTemplateRepository.findByDepartment_Id(department.getId());
            
            for (ShiftTemplate template : templates) {
                for (EmployeeAvailability availability : availabilities) {
                    Employee employee = availability.getEmployee();
                    
                    if (matchesSkills(employee.getSkills(), template.getRequiredSkills())) {
                        GeneratedShiftSchedule schedule = new GeneratedShiftSchedule();
                        schedule.setShiftDate(date);
                        schedule.setStartTime(template.getStartTime());
                        schedule.setEndTime(template.getEndTime());
                        schedule.setEmployee(employee);
                        schedule.setDepartment(department);
                        schedule.setShiftTemplate(template);
                        
                        schedules.add(scheduleRepository.save(schedule));
                        break;
                    }
                }
            }
        }
        
        return schedules;
    }

    @Override
    public List<GeneratedShiftSchedule> getByDate(LocalDate date) {
        return scheduleRepository.findByShiftDate(date);
    }

    private boolean matchesSkills(String employeeSkills, String requiredSkills) {
        if (employeeSkills == null || requiredSkills == null) {
            return false;
        }
        
        List<String> empSkillsList = Arrays.asList(employeeSkills.split(","));
        List<String> reqSkillsList = Arrays.asList(requiredSkills.split(","));
        
        for (String reqSkill : reqSkillsList) {
            boolean found = false;
            String reqSkillTrimmed = reqSkill.trim().toUpperCase();
            
            for (String empSkill : empSkillsList) {
                if (empSkill.trim().toUpperCase().equals(reqSkillTrimmed)) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                return false;
            }
        }
        
        return true;
    }
}





