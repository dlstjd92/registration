package com.inspark.courseregistrationmodule.controller;


import com.inspark.courseregistrationmodule.domain.Student;
import com.inspark.courseregistrationmodule.dto.TutorAvailableTimeDto;
import com.inspark.courseregistrationmodule.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/add") // 학생 추가
    public void addTutor(@RequestBody Student student) {
        studentService.addStudent(student);
    }


    @PostMapping("/todo") // 내가 예약한 수업 보기 TODO
    public boolean removeAvailableTime(@RequestBody TutorAvailableTimeDto tutorAvailableTimeDto) {
        return true;
    }


}
