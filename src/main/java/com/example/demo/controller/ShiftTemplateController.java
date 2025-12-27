package com.example.demo.controller;

import com.example.demo.model.ShiftTemplate;
import com.example.demo.service.ShiftTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Shift Templates Endpoints")
public class ShiftTemplateController {
    private final ShiftTemplateService shiftTemplateService;

    // Requirement: Constructor Injection (Crucial for test compatibility)
    public ShiftTemplateController(ShiftTemplateService shiftTemplateService) {
        this.shiftTemplateService = shiftTemplateService;
    }

    @PostMapping
    @Operation(summary = "Create shift template")
    public ResponseEntity<ShiftTemplate> create(@RequestBody ShiftTemplate template) {
        // Business logic (time validation and uniqueness) is handled in ShiftTemplateServiceImpl
        return ResponseEntity.ok(shiftTemplateService.create(template));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get templates by department")
    public ResponseEntity<List<ShiftTemplate>> getByDepartment(@PathVariable Long departmentId) {
        // FIX: Changed call to findByDepartment to match our Service interface
        return ResponseEntity.ok(shiftTemplateService.findByDepartment(departmentId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shift template")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        shiftTemplateService.delete(id);
        // Test suite consistently expects the "Deleted" string for successful deletions
        return ResponseEntity.ok("Deleted");
    }
}