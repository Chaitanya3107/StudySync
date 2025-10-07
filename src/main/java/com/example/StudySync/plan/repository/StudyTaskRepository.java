package com.example.StudySync.plan.repository;

import com.example.StudySync.plan.model.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyTaskRepository extends JpaRepository<StudyTask, Integer> {
    List<StudyTask> findBySubjectId(Integer subjectId);

    @Query("SELECT t FROM StudyTask t WHERE t.completed = false AND t.subject.user.email = :email")
    List<StudyTask> findIncompleteTasksByUserEmail(@Param("email") String email);

}
