package com.task.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.model.Task;
import com.task.model.TaskStatus;
import com.task.repository.TaskRepository;

@Service
public class TaskServiceImplementation implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task createTask(Task task, String requestRole) throws Exception {
        if (!requestRole.equals("ROLE_ADMIN")) {
            throw new Exception("Only ADMIN can create a task");
        }
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        if (task.getPriority() == null) {
            task.setPriority("medium"); 
        }
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) throws Exception {
        return taskRepository.findById(id).orElseThrow(() -> new Exception("Task not found with id: " + id));
    }

    @Override
    public List<Task> getAllTasks(TaskStatus status) throws Exception {
        List<Task> allTasks = taskRepository.findAll();

        List<Task> filteredTasks = allTasks.stream()
                .filter(task -> status == null || task.getStatus().name().equalsIgnoreCase(status.toString()))
                .collect(Collectors.toList());
        
        return filteredTasks;
    }

    @Override
    public Task updateTask(Long id, Task updatedTask, Long userId) throws Exception {
        Task existingTask = getTaskById(id);

        if (updatedTask.getTitle() != null) {
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getImage() != null) {
            existingTask.setImage(updatedTask.getImage());
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getPriority() != null) {
            existingTask.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getDeadline() != null) {
            existingTask.setDeadline(updatedTask.getDeadline());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }

        return taskRepository.save(existingTask);
    }

    @Override
    public Task patchTaskStatus(Long id, TaskStatus newStatus) throws Exception {
        Task existingTask = getTaskById(id);
    
        if (newStatus != null && !existingTask.getStatus().equals(newStatus)) {
            existingTask.setStatus(newStatus);
        } else {
            throw new Exception("The status provided is the same as the current status or is invalid.");
        }
    
        return taskRepository.save(existingTask);
    }


    @Override
    public void deleteTask(Long id) throws Exception {
        getTaskById(id);
        taskRepository.deleteById(id);
    }

    @Override
    public Task assignedToUser(Long taskId, Long userId) throws Exception {
        Task task = getTaskById(taskId);
        task.setAssignedUserId(userId);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> assignedUsersTask(Long userId, TaskStatus status) throws Exception {
        List<Task> allTasks = taskRepository.findByAssignedUserId(userId);
        return allTasks.stream()
                .filter(task -> status == null || task.getStatus().name().equalsIgnoreCase(status.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public Task completeTask(Long taskId) throws Exception {
        Task task = getTaskById(taskId);
        task.setStatus(TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }
}
