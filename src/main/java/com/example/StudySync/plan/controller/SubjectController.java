package com.example.StudySync.plan.controller;
import com.example.StudySync.plan.dto.SubjectRequest;
import com.example.StudySync.plan.dto.SubjectResponse;
import com.example.StudySync.plan.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject(@RequestBody @Valid SubjectRequest subjectRequest, @AuthenticationPrincipal UserDetails userDetails){
            return ResponseEntity.ok(subjectService.createSubject(subjectRequest,userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getSubject(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(subjectService.getAllSubjects(userDetails.getUsername()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Integer id, @RequestBody @Valid SubjectRequest subjectRequest,@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(subjectService.updateSubject(id,subjectRequest,userDetails.getUsername()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Integer id){
        subjectService.deleteSubject(id);
        return ResponseEntity.ok().build();
    }

}
