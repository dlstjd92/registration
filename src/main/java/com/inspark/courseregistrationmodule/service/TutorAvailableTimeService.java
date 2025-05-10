package com.inspark.courseregistrationmodule.service;

import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import com.inspark.courseregistrationmodule.repository.TutorAvailableTimeRepository;
import com.inspark.courseregistrationmodule.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorAvailableTimeService {

    private final TutorRepository tutorRepository;

    private final TutorAvailableTimeRepository tutorAvailableTimeRepository;

    public void addAvailableTimes(Long tutorId, List<Long> times) { // 튜터가 시간을 추가하는 곳
        // 인풋값으로 튜터의 id와 목표시간을 받음 -> 해당 시간에 수업할 수 있도록 추가해야함.
        for (Long time : times) { // 추가할 시간대를 루프돌기
            if (!tutorAvailableTimeRepository.existsByTutorIdAndTimeBlockId(tutorId, time) ){
                TutorAvailableTime availableTime = TutorAvailableTime.builder()
                        .tutor(tutorRepository.findById(tutorId).orElseThrow())
//                        .timeBlock(timeBlockRepository.findById(time).orElseThrow())
                        .build();

                tutorAvailableTimeRepository.save(availableTime);
            }
        }

    }

    public void removeAvailableTimes(Long tutorId, List<Integer> times) {
        // TODO: 구현 예정
    }
}
