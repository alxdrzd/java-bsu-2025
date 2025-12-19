package com.example.grade_calculator;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "student_grade")
public class StudentGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

    private int score1;
    private int score2;
    private int score3;
    private int score4;

    private int average;

    private String finalVerdict;

    final private LocalDateTime createdAt = LocalDateTime.now();

}
