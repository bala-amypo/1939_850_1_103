package com.example.demo.service.impl;

import com.example.demo.model.EmployeeAvailability;
import com.example.demo.repository.AvailabilityRepository;
import com.example.demo.service.AvailabilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {
    private final AvailabilityRepository availabilityRepository;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public EmployeeAvailability saveAvailability(EmployeeAvailability availability) {
        availabilityRepository.findByEmployee_IdAndAvailableDate(
            availability.getEmployee().getId(), 
            availability.getAvailableDate()
        ).ifPresent(a -> {
            throw new RuntimeException("exists");
        });
        return availabilityRepository.save(availability);
    }

    @Override
    public List<EmployeeAvailability> getByEmployee(Long empId) {
        return availabilityRepository.findByEmployee_Id(empId);
    }

    @Override
    public List<EmployeeAvailability> getByDate(LocalDate date) {
        return availabilityRepository.findByAvailableDateAndAvailable(date, true);
    }

    @Override
    public EmployeeAvailability update(Long id, EmployeeAvailability availability) {
        EmployeeAvailability existing = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
        existing.setAvailable(availability.getAvailable());
        return availabilityRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!availabilityRepository.existsById(id)) {
            throw new RuntimeException("not found");
        }
        availabilityRepository.deleteById(id);
    }
}