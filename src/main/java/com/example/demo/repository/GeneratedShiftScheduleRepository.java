package com.example.demo.repository;

import com.example.demo.model.GeneratedShiftSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GeneratedShiftScheduleRepository extends JpaRepository<GeneratedShiftSchedule, Long> {
    
    // Required by ScheduleService.getByDate (Test Priority 62)
    List<GeneratedShiftSchedule> findByShiftDate(LocalDate shiftDate);

    // Useful for ScheduleService.generateForDate to ensure atomicity
    // Prevents duplicate schedules if the generate action is triggered twice
    void deleteByShiftDate(LocalDate shiftDate);

    // Used for reporting or employee-specific schedule views
    List<GeneratedShiftSchedule> findByEmployee_Id(Long employeeId);
}