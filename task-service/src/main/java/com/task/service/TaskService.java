package com.task.service;

import com.task.model.Task;
import com.task.model.TaskStatus;

import java.util.List;

public interface TaskService {

    Task createTask(Task task, String requestRole) throws Exception;

    Task getTaskById(Long id) throws Exception;

    List<Task> getAllTasks(TaskStatus status) throws Exception;

    Task updateTask(Long id, Task task, Long userId) throws Exception;

    void deleteTask(Long id) throws Exception;

    List<Task> assignedUsersTask(Long userId, TaskStatus status) throws Exception;

    Task assignedToUser(Long taskId, Long userId) throws Exception;

    Task completeTask(Long taskId) throws Exception;

    Task patchTaskStatus(Long id, TaskStatus newStatus) throws Exception;
}
