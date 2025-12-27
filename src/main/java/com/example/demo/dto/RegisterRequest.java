package com.example.demo.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String skills;
    private Integer maxHoursPerWeek;

    public RegisterRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Integer getMaxHoursPerWeek() {
        return maxHoursPerWeek;
    }

    public void setMaxHoursPerWeek(Integer maxHoursPerWeek) {
        this.maxHoursPerWeek = maxHoursPerWeek;
    }
}
