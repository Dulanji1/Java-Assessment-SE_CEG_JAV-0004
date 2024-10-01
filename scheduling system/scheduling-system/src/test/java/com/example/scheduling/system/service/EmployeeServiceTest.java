//package com.example.scheduling.system.service;
//
//import com.example.scheduling.system.dto.EmployeeDTO;
//import com.example.scheduling.system.entity.Employee;
//import com.example.scheduling.system.repository.EmployeeRepo;
//import com.example.scheduling.system.util.VarList;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeToken;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//public class EmployeeServiceTest {
//
//    @InjectMocks
//    private EmployeeService employeeService;
//
//    @Mock
//    private EmployeeRepo employeeRepo;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    private EmployeeDTO employeeDTO;
//    private Employee employee;
//
//
//
//    @Test
//    public void testSaveEmployee_Duplicate() {
//        when(employeeRepo.existsById(employeeDTO.getEmpID())).thenReturn(true);
//        String response = employeeService.saveEmployee(employeeDTO);
//        assertEquals(VarList.RSP_DUPLICATED, response);
//    }
//
//    @Test
//    public void testSaveEmployee_Success() {
//        when(employeeRepo.existsById(employeeDTO.getEmpID())).thenReturn(false);
//        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);
//        String response = employeeService.saveEmployee(employeeDTO);
//        verify(employeeRepo).save(any(Employee.class));
//        assertEquals(VarList.RSP_SUCCESS, response);
//    }
//
//    @Test
//    public void testUpdateEmployee_Exists() {
//        when(employeeRepo.existsById(employeeDTO.getEmpID())).thenReturn(true);
//        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);
//        String response = employeeService.updateEmployee(employeeDTO);
//        verify(employeeRepo).save(any(Employee.class));
//        assertEquals(VarList.RSP_SUCCESS, response);
//    }
//
//    @Test
//    public void testUpdateEmployee_NotFound() {
//        when(employeeRepo.existsById(employeeDTO.getEmpID())).thenReturn(false);
//        String response = employeeService.updateEmployee(employeeDTO);
//        assertEquals(VarList.RSP_NO_DATA_FOUND, response);
//    }
//
//
//    @Test
//    public void testSearchEmployee_Found() {
//        when(employeeRepo.existsById(anyInt())).thenReturn(true);
//        when(employeeRepo.findById(anyInt())).thenReturn(Optional.of(employee));
//        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);
//        EmployeeDTO result = employeeService.searchEmployee(employeeDTO.getEmpID());
//        assertEquals(employeeDTO.getEmpID(), result.getEmpID());
//    }
//
//    @Test
//    public void testSearchEmployee_NotFound() {
//        when(employeeRepo.existsById(anyInt())).thenReturn(false);
//        EmployeeDTO result = employeeService.searchEmployee(employeeDTO.getEmpID());
//        assertEquals(null, result);
//    }
//
//    @Test
//    public void testDeleteEmployee_Exists() {
//        when(employeeRepo.existsById(employeeDTO.getEmpID())).thenReturn(true);
//        String response = employeeService.deleteEmployee(employeeDTO.getEmpID());
//        verify(employeeRepo).deleteById(employeeDTO.getEmpID());
//        assertEquals(VarList.RSP_SUCCESS, response);
//    }
//
//    @Test
//    public void testDeleteEmployee_NotFound() {
//        when(employeeRepo.existsById(employeeDTO.getEmpID())).thenReturn(false);
//        String response = employeeService.deleteEmployee(employeeDTO.getEmpID());
//        assertEquals(VarList.RSP_NO_DATA_FOUND, response);
//    }
//}
