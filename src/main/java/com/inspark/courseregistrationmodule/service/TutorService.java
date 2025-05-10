package com.inspark.courseregistrationmodule.service;

import com.inspark.courseregistrationmodule.domain.Tutor;
import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import com.inspark.courseregistrationmodule.dto.TutorAvailableTimeDto;
import com.inspark.courseregistrationmodule.repository.TutorAvailableTimeRepository;
import com.inspark.courseregistrationmodule.repository.TutorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;

    private final TutorAvailableTimeRepository tutorAvailableTimeRepository;


    // 튜터추가
    public void addTutor(Tutor tutor) {
        // 인수가 null 혹은 tutor의 이메일이 비어있으면
        if (tutor == null || tutor.getEmail() == null)
            throw new RuntimeException("Tutor cannot be null");

        // 이미 존재하는 튜터인지 확인 (이메일 기준)
        boolean exists = tutorRepository.existsByEmail(tutor.getEmail());
        if (exists)
            throw new RuntimeException("email already exists");

        tutorRepository.save(tutor);
    }

    @Transactional
    public void addAvailableTimes(TutorAvailableTimeDto tutorAvailableTimeDto) {
        if (tutorAvailableTimeDto == null)
            throw new RuntimeException("availableTutorDto cannot be null");

        List<ZonedDateTime> times = tutorAvailableTimeDto.times();
        Long tutorId = tutorAvailableTimeDto.tutorId();

        for (ZonedDateTime time : times) {
            // 해당 시간대가 존재한다면
            if (tutorAvailableTimeRepository.existsByTutorIdAndAvailableTime(tutorId, time)) {
                throw new RuntimeException("Trying to add already exist available time(s)" + time);
            }

            TutorAvailableTime availableTime = TutorAvailableTime.builder()
                    .tutor(tutorRepository.findById(tutorId).orElseThrow())
                    .availableTime(time)
                    .build();
            tutorAvailableTimeRepository.save(availableTime);
        }
    }

    @Transactional
    public void removeAvailableTimes(TutorAvailableTimeDto tutorAvailableTimeDto) {
        if (tutorAvailableTimeDto == null)
            throw new RuntimeException("availableTutorDto cannot be null");

        List<ZonedDateTime> times = tutorAvailableTimeDto.times();
        Long tutorId = tutorAvailableTimeDto.tutorId();

        for (ZonedDateTime time : times) {
            // 만약 해당 시간대가 존재하면
            if (!tutorAvailableTimeRepository.existsByTutorIdAndAvailableTime(tutorId, time)) {
                throw new RuntimeException("Trying to remove unregistered available time(s)" + time);
            }
            tutorAvailableTimeRepository.deleteByTutorIdAndAvailableTime(tutorId, time);
        }
    }
}
