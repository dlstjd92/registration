package com.inspark.courseregistrationmodule.service;

import com.inspark.courseregistrationmodule.domain.Student;
import com.inspark.courseregistrationmodule.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public boolean addLesson(Student student){


        return true;
    }

}
