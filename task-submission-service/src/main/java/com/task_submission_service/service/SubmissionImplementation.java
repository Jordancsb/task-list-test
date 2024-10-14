package com.task_submission_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task_submission_service.model.Submission;
import com.task_submission_service.model.TaskDto;
import com.task_submission_service.repository.SubmissionRepository;

@Service
public class SubmissionImplementation implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Override
    public Submission submitTask(Long taskId, String githubLink, Long userId, String token) throws Exception {
        TaskDto task = taskService.getTaskById(taskId, token);
        if (task != null) {
            Submission submission = new Submission();
            submission.setTaskId(taskId);
            submission.setGithubLink(githubLink);
            submission.setUserId(userId);
            submission.setStatus("PENDING");
            submission.setSubmissionTime(LocalDateTime.now());

            return submissionRepository.save(submission);
        }
        throw new Exception("Task not found with id: " + taskId);
    }

    @Override
    public Submission getTaskSubmissionById(Long submissionId) throws Exception {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new Exception("Submission not found with id: " + submissionId));
    }

    @Override
    public List<Submission> getAllTaskSubmissions() {
        return submissionRepository.findAll();
    }

    @Override
    public List<Submission> getAllTaskSubmissionsByTaskId(Long taskId) {
        return submissionRepository.findByTaskId(taskId);
    }

    @Override
    public Submission acceptDeclineSubmission(Long submissionId, String status) throws Exception {
        Submission submission = getTaskSubmissionById(submissionId);
        submission.setStatus(status);
        if (status.equals("ACCEPT")) {
            taskService.completeTask(submissionId, status);
        }
        return submissionRepository.save(submission);
    }
}
