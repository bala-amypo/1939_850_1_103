package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvailabilityDto {
    
    @NotNull(message = "Availability status is required")
    private Boolean available;

    @NotNull(message = "Available date is required")
    private LocalDate availableDate;

    // Default constructor for JSON deserialization
    public AvailabilityDto() {}

    public AvailabilityDto(Boolean available, LocalDate availableDate) {
        this.available = available;
        this.availableDate = availableDate;
    }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public LocalDate getAvailableDate() { return availableDate; }
    public void setAvailableDate(LocalDate availableDate) { this.availableDate = availableDate; }
}