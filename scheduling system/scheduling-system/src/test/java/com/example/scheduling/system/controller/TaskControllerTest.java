package com.example.scheduling.system.controller;

import com.example.scheduling.system.entity.Task;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.TaskRepository;
import com.example.scheduling.system.repository.UserRepository;
import com.example.scheduling.system.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskService taskService;

    private Task task;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
       // user = new User(1L, "Test User", "test@example.com", "password", "USER", "ACTIVE");
        task = new Task();
    }



    @Test
    public void testUpdateTask_TaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());
        ResponseEntity<String> response = taskController.updateTask(task.getId(), task);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task with ID: " + task.getId() + " not found. Please check the ID and try again.", response.getBody());
    }


    @Test
    public void testDeleteTask_TaskNotFound() {
        when(taskRepository.existsById(task.getId())).thenReturn(false);
        ResponseEntity<String> response = taskController.deleteTask(task.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task with ID: " + task.getId() + " not found. Deletion cannot be performed.", response.getBody());
    }

    @Test
    public void testDeleteTask_Success() {
        when(taskRepository.existsById(task.getId())).thenReturn(true);
        ResponseEntity<String> response = taskController.deleteTask(task.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task with ID: " + task.getId() + " deleted successfully.", response.getBody());
    }

    @Test
    public void testGetTaskById_TaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());
        ResponseEntity<?> response = taskController.getTaskById(task.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task with ID: " + task.getId() + " not found.", response.getBody());
    }

    @Test
    public void testGetTaskById_Success() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        ResponseEntity<?> response = taskController.getTaskById(task.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    public void testGetAllTasks_NoTasksAvailable() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList());
        ResponseEntity<?> response = taskController.getAllTasks();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No tasks available in the system.", response.getBody());
    }


}
