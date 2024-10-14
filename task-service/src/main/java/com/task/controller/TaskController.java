package com.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.task.model.Task;
import com.task.model.TaskStatus;
import com.task.model.UserDto;
import com.task.service.TaskServiceImplementation;
import com.task.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskServiceImplementation taskService;

    @Autowired
    private UserService userService;
    
    @CrossOrigin(origins = "http://localhost:5173/")
    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String token) throws Exception {
        UserDto user = userService.getUserProfile(token); 
        Task createdTask = taskService.createTask(task, user.getRole());

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, @RequestHeader("Authorization") String token) throws Exception {
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> getAssignedUsersTask(
            @RequestParam(required = false) TaskStatus status,
            @RequestHeader("Authorization") String token) throws Exception {
        UserDto user = userService.getUserProfile(token);
        List<Task> tasks = taskService.assignedUsersTask(user.getId(), status);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173/")
    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestHeader("Authorization") String token) throws Exception {
    
        List<Task> tasks = taskService.getAllTasks(status); 
    
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{id}/user/{userId}/assigned")
    public ResponseEntity<Task> assignedTaskToUser(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) throws Exception {
        
        Task task = taskService.assignedToUser(id, userId);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173/")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task req,
            @RequestHeader("Authorization") String token) throws Exception {
        UserDto user = userService.getUserProfile(token);
        
        Task task = taskService.updateTask(id, req, user.getId());

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) throws Exception {
        
        Task task = taskService.completeTask(id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173/")
    @PatchMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) throws Exception {
        Task updatedTask = taskService.patchTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws Exception {
        
        taskService.deleteTask(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
