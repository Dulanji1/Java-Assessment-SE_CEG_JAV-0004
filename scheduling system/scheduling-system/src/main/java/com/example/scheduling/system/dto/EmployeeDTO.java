package com.example.scheduling.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmployeeDTO {

    private int empID;  // Assuming empID is an integer

    @NotBlank(message = "Employee Name cannot be blank")
    private String empName;

    @NotBlank(message = "Employee Address cannot be blank")
    private String empAddress;

    @NotBlank(message = "Employee Mobile Number cannot be blank")
    private String empMobileNumber;

    @NotBlank(message = "Employee Role cannot be blank")
    private String empRole;

    @NotBlank(message = "Employee Email cannot be blank")
    @Email(message = "Email should be valid")
    private String empMail;

    // Getters and Setters
    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpMobileNumber() {
        return empMobileNumber;
    }

    public void setEmpMobileNumber(String empMobileNumber) {
        this.empMobileNumber = empMobileNumber;
    }

    public String getEmpRole() {
        return empRole;
    }

    public void setEmpRole(String empRole) {
        this.empRole = empRole;
    }

    public String getEmpMail() {
        return empMail;
    }

    public void setEmpMail(String empMail) {
        this.empMail = empMail;
    }
}
