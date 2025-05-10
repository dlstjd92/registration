package com.inspark.courseregistrationmodule.controller;


import com.inspark.courseregistrationmodule.domain.Student;
import com.inspark.courseregistrationmodule.dto.TutorAvailableTimeDto;
import com.inspark.courseregistrationmodule.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/add") // 학생 추가
    public void addTutor(@RequestBody Student student) {
        studentService.addStudent(student);
    }

    @GetMapping("/findTime") // 기간 & 수업 길이로 현재 수업 가능한 시간대를 조회
    public List<ZonedDateTime> findTimeWithDurationAndClassLength(
            @RequestParam ZonedDateTime start,
            @RequestParam ZonedDateTime end,
            @RequestParam int classLength
    ) {
        return studentService.findTimeWithDurationAndClassLength(start, end, classLength);
    }


    @PostMapping("/todo") // 내가 예약한 수업 보기 TODO
    public boolean removeAvailableTime(@RequestBody TutorAvailableTimeDto tutorAvailableTimeDto) {
        return true;
    }


}
