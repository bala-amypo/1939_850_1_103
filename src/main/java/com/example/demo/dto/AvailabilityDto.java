package com.example.demo.dto;

import java.time.LocalDate;

public class AvailabilityDto {
    private Boolean available;
    private LocalDate availableDate;

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