    package com.example.StudySync.plan.model;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class StudyTask {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String title;
        private String notes;
        private LocalDate dueDate;
        @Column(nullable = false)
        private boolean completed = false;
        @ManyToOne
        private Subject subject;
    }








