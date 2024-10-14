package com.task_submission_service.service;

import java.util.List;
import com.task_submission_service.model.Submission;

public interface SubmissionService {

    Submission submitTask(Long taskId, String githubLink, Long userId, String token) throws Exception;

    Submission getTaskSubmissionById(Long submissionId) throws Exception;

    List<Submission> getAllTaskSubmissions();

    List<Submission> getAllTaskSubmissionsByTaskId(Long taskId);

    Submission acceptDeclineSubmission(Long submissionId, String status) throws Exception;

}
