package com.example.StudySync.plan.service;
import com.example.StudySync.auth.model.User;
import com.example.StudySync.auth.repository.UserRepository;
import com.example.StudySync.plan.dto.SubjectRequest;
import com.example.StudySync.plan.dto.SubjectResponse;
import com.example.StudySync.plan.model.Subject;
import com.example.StudySync.plan.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    UserRepository userRepository;

    public SubjectResponse createSubject(@Valid SubjectRequest subjectRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setDescription(subjectRequest.getDescription());
        subject.setUser(user);
        subject.setName(subjectRequest.getName());
        subjectRepository.save(subject);
        return new SubjectResponse(subject.getId(),subject.getName(),subject.getDescription());
    }

    public List<SubjectResponse> getAllSubjects(String email) {

        List<Subject> subjectList = subjectRepository.findByUserEmail(email);
//        .stream() starts processing each one item of subjectList:
//        First iteration: s = Subject with ID 1
//        Second iteration: s = Subject with ID 2
//                .map(...) transforms each Subject into a new SubjectResponse.
         return subjectList.stream().map(s->new SubjectResponse(s.getId(),s.getName(),s.getDescription())).toList();
    }

    public SubjectResponse updateSubject(Integer id ,@Valid SubjectRequest subjectRequest, String email) {
        Subject subject = subjectRepository.findById(id).filter(s->s.getUser().getEmail().equals(email)).orElseThrow(()->new RuntimeException("Subject not found"));
        subject.setName(subjectRequest.getName());
        subject.setDescription(subjectRequest.getDescription());
        subjectRepository.save(subject);
        return new SubjectResponse(subject.getId(),subject.getName(),subject.getDescription());
    }

    public void deleteSubject(Integer id) {
        subjectRepository.deleteById(id);
    }
}
