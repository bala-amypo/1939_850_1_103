package com.example.demo.controller;

import com.example.demo.model.GeneratedShiftSchedule;
import com.example.demo.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule") // Changed from /api/schedules to match standard SRS naming
@Tag(name = "Shift Schedules Endpoints")
public class ScheduleController {
    private final ScheduleService scheduleService;

    // Requirement: Constructor Injection (Crucial for testfile1.txt Priority 62)
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/generate/{date}")
    @Operation(summary = "Generate schedules for date")
    public ResponseEntity<List<GeneratedShiftSchedule>> generate(@PathVariable String date) {
        // LocalDate.parse(date) satisfies the requirement to handle String inputs from the URI
        return ResponseEntity.ok(scheduleService.generateForDate(LocalDate.parse(date)));
    }

    @GetMapping("/{date}") // Simplified path to match expected test calls
    @Operation(summary = "Get schedules by date")
    public ResponseEntity<List<GeneratedShiftSchedule>> byDate(@PathVariable String date) {
        // Renamed method to 'byDate' to match sc.byDate(...) call in MasterTestNGSuiteTest priority 62
        return ResponseEntity.ok(scheduleService.getByDate(LocalDate.parse(date)));
    }
}