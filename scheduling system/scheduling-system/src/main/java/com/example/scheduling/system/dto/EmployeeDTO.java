package com.example.scheduling.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
//@Data
public class EmployeeDTO {
    private int empID;
    private String empName;
    private String empAddress;
    private String empMNumber;

    
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

    public String getEmpMNumber() {
        return empMNumber;
    }

    public void setEmpMNumber(String empMNumber) {
        this.empMNumber = empMNumber;
    }
}
