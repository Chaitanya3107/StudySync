package com.example.StudySync.plan.service;

import com.example.StudySync.plan.dto.TaskRequest;
import com.example.StudySync.plan.dto.TaskResponse;
import com.example.StudySync.plan.model.StudyTask;
import com.example.StudySync.plan.model.Subject;
import com.example.StudySync.plan.repository.StudyTaskRepository;
import com.example.StudySync.plan.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyTaskService {

    private final SubjectRepository subjectRepository;
    private final StudyTaskRepository taskRepository;

    public TaskResponse addTask(Integer subjectId, TaskRequest request, String userEmail) {
        Subject subject = subjectRepository.findById(subjectId)
                .filter(s -> s.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new RuntimeException("Subject not found or unauthorized"));
        if (request.getDueDate() != null && request.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be before today");
        }
        StudyTask task = new StudyTask();
        task.setTitle(request.getTitle());
        task.setNotes(request.getNotes());
        task.setDueDate(request.getDueDate());
        task.setSubject(subject);
        task.setCompleted(false);
        StudyTask saved = taskRepository.save(task);

        return new TaskResponse(saved.getId(), saved.getTitle(), saved.getNotes(), saved.isCompleted(), saved.getDueDate());
    }

    public List<TaskResponse> getTasks(Integer subjectId, String userEmail) {
        Subject subject = subjectRepository.findById(subjectId)
                .filter(s -> s.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new RuntimeException("Unauthorized or subject not found"));

        return taskRepository.findBySubjectId(subjectId).stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getNotes(), task.isCompleted(), task.getDueDate()))
                .toList();
    }

    public void deleteTask(Integer taskId, String userEmail) {
        StudyTask task = taskRepository.findById(taskId)
                .filter(t -> t.getSubject().getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new RuntimeException("Task not found or unauthorized"));
        taskRepository.delete(task);
    }

    public void markComplete(Integer taskId, String userEmail) {
        StudyTask task = taskRepository.findById(taskId)
                .filter(t -> t.getSubject().getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new RuntimeException("Task not found or unauthorized"));

        task.setCompleted(true);
        taskRepository.save(task);
    }
}
