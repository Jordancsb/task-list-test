package com.task.repository;

import com.task.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task1;
    private Task task2;

    @BeforeEach
    public void setUp() {
        task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description for Task 1");
        task1.setAssignedUserId(1L);

        task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description for Task 2");
        task2.setAssignedUserId(2L);

        taskRepository.save(task1);
        taskRepository.save(task2);
    }

    @Test
    void testFindByAssignedUserId() {
        List<Task> tasksForUser1 = taskRepository.findByAssignedUserId(1L);

        assertThat(tasksForUser1).hasSize(1);
        assertThat(tasksForUser1.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(tasksForUser1.get(0).getAssignedUserId()).isEqualTo(1L);
    }

    @Test
    void testFindByAssignedUserIdNotFound() {
        List<Task> tasksForNonexistentUser = taskRepository.findByAssignedUserId(999L);
        assertThat(tasksForNonexistentUser).isEmpty();
    }
}
