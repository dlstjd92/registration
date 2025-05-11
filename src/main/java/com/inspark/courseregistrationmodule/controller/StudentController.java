package com.inspark.courseregistrationmodule.controller;


import com.inspark.courseregistrationmodule.domain.Reservation;
import com.inspark.courseregistrationmodule.domain.Student;
import com.inspark.courseregistrationmodule.domain.Tutor;
import com.inspark.courseregistrationmodule.dto.ReservationRequestDto;
import com.inspark.courseregistrationmodule.dto.ReservationResponseDto;
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
        return studentService.findStartTimesWithDurationAndClassLength(start, end, classLength);
    }

    @GetMapping("/findTutor") // 시간 & 수업 길이로 수업 가능한 튜터 조회
    public List<Tutor> findTutorWithTimeAndClassLength(
            @RequestParam ZonedDateTime time,
            @RequestParam int classLength
    ){
        return studentService.findTutorWithTimeAndClassLength(time, classLength);
    }

    @PostMapping("/reservation") // 예약 하기
    public void makeReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        studentService.makeReservation(reservationRequestDto);
    }

    @GetMapping("/myLesson") // 내가 예약한 수업 보기
    public List<ReservationResponseDto> findMyLessonWithEmail(@RequestParam String email) {
        return studentService.getReservationsByStudent(email);
    }




}
