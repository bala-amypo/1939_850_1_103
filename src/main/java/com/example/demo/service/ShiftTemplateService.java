package com.example.demo.service;

import com.example.demo.model.ShiftTemplate;
import java.util.List;

public interface ShiftTemplateService {
    ShiftTemplate create(ShiftTemplate shiftTemplate);
    List<ShiftTemplate> getByDepartment(Long departmentId);
    List<ShiftTemplate> getAll();
}

UserService.java:
package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {
    User register(User user);
    User findByEmail(String email);
}