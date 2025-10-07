package com.example.StudySync.plan.repository;

import com.example.StudySync.auth.model.User;
import com.example.StudySync.plan.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {

// this method finds Subject that are created by user
    List<Subject> findByUserEmail(String email);
}
