package com.example.scheduling.system.controller;

import com.example.scheduling.system.dto.EmployeeDTO;
import com.example.scheduling.system.dto.ResponseDTO;
import com.example.scheduling.system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/employee")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // In-memory store for employee names (for demo purposes)
    private Set<String> employeeNames = new HashSet<>();
    // Improved email pattern for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    // saveEmployee
    @PostMapping
    public ResponseEntity<ResponseDTO> saveEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        // Check for null values in EmployeeDTO
        if (employeeDTO == null || employeeDTO.getEmpID() <= 0 || employeeDTO.getEmpName() == null || employeeDTO.getEmpMail() == null) {
            return buildResponseEntity(HttpStatus.BAD_REQUEST, "Invalid input: Employee details are missing or null.", null);
        }

        // Check for email format
        if (!isEmailValid(employeeDTO.getEmpMail())) {
            return buildResponseEntity(HttpStatus.BAD_REQUEST, "Invalid email format.", null);
        }

        // Check for duplicate employee names
        if (employeeNames.contains(employeeDTO.getEmpName())) {
            return buildResponseEntity(HttpStatus.BAD_REQUEST, "Employee name already exists.", employeeDTO);
        } else {
            employeeNames.add(employeeDTO.getEmpName()); // Add the name to the set
        }

        try {
            String res = employeeService.saveEmployee(employeeDTO);
            switch (res) {
                case "00":
                    return buildResponseEntity(HttpStatus.CREATED, "Success", employeeDTO);
                case "06":
                    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Employee already registered under this Employee Number", employeeDTO);
                default:
                    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Error occurred", null);
            }
        } catch (Exception ex) {
            return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
        }
    }

    // updateEmployee
    @PutMapping
    public ResponseEntity<ResponseDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        // Check for null values in EmployeeDTO
        if (employeeDTO == null || employeeDTO.getEmpID() <= 0 || employeeDTO.getEmpMail() == null) {
            return buildResponseEntity(HttpStatus.BAD_REQUEST, "Invalid input: Employee details are missing or null.", null);
        }

        // Check for email format
        if (!isEmailValid(employeeDTO.getEmpMail())) {
            return buildResponseEntity(HttpStatus.BAD_REQUEST, "Invalid email format.", null);
        }

        // Check if the employee name is being changed and if it already exists
        if (employeeNames.contains(employeeDTO.getEmpName())) {
            return buildResponseEntity(HttpStatus.BAD_REQUEST, "Employee name already exists.", employeeDTO);
        }

        try {
            String res = employeeService.updateEmployee(employeeDTO);
            switch (res) {
                case "00":
                    return buildResponseEntity(HttpStatus.ACCEPTED, "Success", employeeDTO);
                case "01":
                    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Not a registered employee", employeeDTO);
                default:
                    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Error occurred", null);
            }
        } catch (Exception ex) {
            return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
        }
    }

    // Helper method to check email format
    private boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Helper method to build ResponseEntity
    private ResponseEntity<ResponseDTO> buildResponseEntity(HttpStatus status, String message, Object content) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(status.value());
        responseDTO.setMessage(message);
        responseDTO.setContent(content);
        return new ResponseEntity<>(responseDTO, status);
    }
}
