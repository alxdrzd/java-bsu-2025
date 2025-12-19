package com.example.grade_calculator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<StudentGrade, Long> {
    boolean existsByName(String name);
}
