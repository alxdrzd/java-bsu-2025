package com.example.grade_calculator;

import ch.qos.logback.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class GradeController {
    @Autowired
    private GradeRepository repository;

    @PostMapping("/calculate")
    public StudentGrade calculate(@RequestBody StudentGrade grade) {

        if (!StringUtils.hasText(grade.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not a valid name!");
        }

        if (repository.existsByName(grade.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка: Студент с именем '" + grade.getName() + "' уже существует!");
        }

        if (isInvalidScore(grade.getScore1())
                || isInvalidScore(grade.getScore2())
                || isInvalidScore(grade.getScore3())
                || isInvalidScore(grade.getScore4())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not a valid mark!");
        }

        double avg = (double) (grade.getScore1() + grade.getScore2() + grade.getScore3() + grade.getScore4()) / 4;
        grade.setAverage((int) Math.round(avg));

        if (avg >= 8) grade.setFinalVerdict("Отлично");
        else if (avg >= 4) grade.setFinalVerdict("Сдал");
        else grade.setFinalVerdict("На пересдачу :(");

        return repository.save(grade);
    }

    @GetMapping("/history")
    public List<StudentGrade> getAll() {
        return repository.findAll();
    }

    private boolean isInvalidScore(int score) {
        return score < 0 || score > 10;
    }
}
