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

    public ShiftTemplateController(ShiftTemplateService shiftTemplateService) {
        this.shiftTemplateService = shiftTemplateService;
    }

    @PostMapping("/department/{departmentId}")
    @Operation(summary = "Create shift template")
    public ResponseEntity<ShiftTemplate> create(@PathVariable Long departmentId, @RequestBody ShiftTemplate template) {
        return ResponseEntity.ok(shiftTemplateService.create(template));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get templates by department")
    public ResponseEntity<List<ShiftTemplate>> getByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(shiftTemplateService.getByDepartment(departmentId));
    }
}