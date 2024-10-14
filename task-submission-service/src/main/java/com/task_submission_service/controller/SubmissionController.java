package com.task_submission_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task_submission_service.service.TaskService;
import com.task_submission_service.service.UserService;
import com.task_submission_service.model.Submission;
import com.task_submission_service.model.UserDto;
import com.task_submission_service.service.SubmissionService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Submission> submitTask(
            @RequestParam Long task_id,
            @RequestParam String github_link,
            @RequestHeader("Authorization") String token) throws Exception {
        UserDto user = userService.getUserProfile(token);
        Submission submission = submissionService.submitTask(task_id, github_link, user.getId(), token);

        return new ResponseEntity<>(submission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) throws Exception {
        Submission submission = submissionService.getTaskSubmissionById(id);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Submission>> getAllSubmissions(
            @RequestHeader("Authorization") String token) throws Exception {
        List<Submission> submissions = submissionService.getAllTaskSubmissions();
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @GetMapping("task/{taskId}")
    public ResponseEntity<List<Submission>> getAllTaskSubmissions(
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String token) throws Exception {
        List<Submission> submissions = submissionService.getAllTaskSubmissionsByTaskId(taskId);
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @PutMapping("/{submissionId}")
    public ResponseEntity<Submission> acceptDeclineSubmission(
            @PathVariable Long submissionId,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) throws Exception {
        Submission submission = submissionService.acceptDeclineSubmission(submissionId, status);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }
}

