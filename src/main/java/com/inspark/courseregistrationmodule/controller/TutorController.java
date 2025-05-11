package com.inspark.courseregistrationmodule.controller;

import com.inspark.courseregistrationmodule.domain.Tutor;
import com.inspark.courseregistrationmodule.dto.TutorAvailableTimeDto;
import com.inspark.courseregistrationmodule.service.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tutor")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @PostMapping("/add") // 튜터 추가
    public void addTutor(@RequestBody Tutor tutor) {
        tutorService.addTutor(tutor);
    }

    @PostMapping("/addAvailableTime") // 튜터 가능 시간대 추가
    public void addAvailableTime(@RequestBody TutorAvailableTimeDto tutorAvailableTimeDto) {
        tutorService.addAvailableTimes(tutorAvailableTimeDto);
    }

    @PostMapping("/removeAvailableTime")
    public void removeAvailableTime(@RequestBody TutorAvailableTimeDto tutorAvailableTimeDto) {
        tutorService.removeAvailableTimes(tutorAvailableTimeDto);
    }


}
