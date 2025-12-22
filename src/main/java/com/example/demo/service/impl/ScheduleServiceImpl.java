package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    private final ShiftTemplateRepository shiftTemplateRepository;
    private final AvailabilityRepository availabilityRepository;
    private final EmployeeRepository employeeRepository;
    private final GeneratedShiftScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ShiftTemplateRepository shiftTemplateRepository,
                              AvailabilityRepository availabilityRepository,
                              EmployeeRepository employeeRepository,
                              GeneratedShiftScheduleRepository scheduleRepository) {
        this.shiftTemplateRepository = shiftTemplateRepository;
        this.availabilityRepository = availabilityRepository;
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<GeneratedShiftSchedule> generateForDate(LocalDate date) {
        List<ShiftTemplate> templates = shiftTemplateRepository.findAll();
        List<EmployeeAvailability> availableEmployees = availabilityRepository.findByAvailableDateAndAvailable(date, true);
        List<GeneratedShiftSchedule> schedules = new ArrayList<>();

        for (ShiftTemplate template : templates) {
            Employee assignedEmployee = findQualifiedEmployee(template, availableEmployees);
            if (assignedEmployee != null) {
                GeneratedShiftSchedule schedule = new GeneratedShiftSchedule(
                    date, template.getStartTime(), template.getEndTime(),
                    template, template.getDepartment(), assignedEmployee
                );
                schedules.add(scheduleRepository.save(schedule));
            }
        }
        return schedules;
    }

    private Employee findQualifiedEmployee(ShiftTemplate template, List<EmployeeAvailability> availableEmployees) {
        List<String> requiredSkills = template.getRequiredSkills() != null ? 
            Arrays.asList(template.getRequiredSkills().split(",")) : List.of();
        
        for (EmployeeAvailability availability : availableEmployees) {
            Employee employee = availability.getEmployee();
            List<String> employeeSkills = employee.getSkillsList();
            
            if (employeeSkills.containsAll(requiredSkills)) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public List<GeneratedShiftSchedule> getByDate(LocalDate date) {
        return scheduleRepository.findByShiftDate(date);
    }
}