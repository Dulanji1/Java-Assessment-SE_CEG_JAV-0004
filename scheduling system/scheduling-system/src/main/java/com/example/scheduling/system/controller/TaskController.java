package com.example.scheduling.system.controller;

import com.example.scheduling.system.entity.Task;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.TaskRepository;
import com.example.scheduling.system.repository.UserRepository;
import com.example.scheduling.system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    // Create Task
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        // Ensure the user exists before assigning
        Optional<User> user = userRepository.findById(task.getUserId());
        if (!user.isPresent()) {
            return new ResponseEntity<>("User not found for User ID: " + task.getUserId(), HttpStatus.BAD_REQUEST);
        }

        // Save the task
        Task createdTask = taskRepository.save(task);

        // Send notification email (if needed)
        String emailResponse = taskService.sendTaskCreationEmail(user.get(), createdTask);

        return new ResponseEntity<>(
                "Task created successfully! Task ID: " + createdTask.getId() +
                        ", Name: " + createdTask.getName() +
                        ", Scheduled Time: " + createdTask.getScheduledTime() +
                        ", Email Status: " + emailResponse,
                HttpStatus.CREATED);
    }

    // Get Tasks by User ID
    @GetMapping("/{userId}")
    public ResponseEntity<?> getTasksByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>("No tasks found for User ID: " + userId, HttpStatus.OK);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Update Task
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (!existingTask.isPresent()) {
            return new ResponseEntity<>(
                    "Task with ID: " + id + " not found. Please check the ID and try again.",
                    HttpStatus.NOT_FOUND);
        }
        task.setId(id);
        Task updatedTask = taskRepository.save(task);

        // Optionally, send notification email
        Optional<User> user = userRepository.findById(updatedTask.getUserId());
        String emailResponse = user.map(value -> taskService.sendTaskUpdateEmail(value, updatedTask)).orElse("No user found for this task.");

        return new ResponseEntity<>(
                "Task updated successfully! Task ID: " + updatedTask.getId() +
                        ", Updated Name: " + updatedTask.getName() +
                        ", Updated Scheduled Time: " + updatedTask.getScheduledTime() +
                        ", Email Status: " + emailResponse,
                HttpStatus.OK);
    }

    // Delete Task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return new ResponseEntity<>(
                    "Task with ID: " + id + " not found. Deletion cannot be performed.",
                    HttpStatus.NOT_FOUND);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity<>(
                "Task with ID: " + id + " deleted successfully.",
                HttpStatus.OK);
    }

    // Get Task by ID
    @GetMapping("/task/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Task with ID: " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }
    }

    // Get All Tasks
    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return new ResponseEntity<>("No tasks available in the system.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}
