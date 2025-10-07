package com.example.StudySync.plan.controller;

import com.example.StudySync.plan.dto.TaskRequest;
import com.example.StudySync.plan.dto.TaskResponse;
import com.example.StudySync.plan.service.StudyTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans/task")
@RequiredArgsConstructor
public class StudyTaskController {

    private final StudyTaskService studyTaskService;

    @PostMapping("/{subjectId}")
    public ResponseEntity<?> create(@PathVariable Integer subjectId,
                                    @RequestBody @Valid TaskRequest taskRequest,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            TaskResponse response = studyTaskService.addTask(subjectId, taskRequest, userDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getTasks(@PathVariable Integer subjectId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<TaskResponse> tasks = studyTaskService.getTasks(subjectId, userDetails.getUsername());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> delete(@PathVariable Integer taskId,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            studyTaskService.deleteTask(taskId, userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<?> markComplete(@PathVariable Integer taskId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        try {
            studyTaskService.markComplete(taskId, userDetails.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Error: " + e.getMessage());
        }
    }
}
