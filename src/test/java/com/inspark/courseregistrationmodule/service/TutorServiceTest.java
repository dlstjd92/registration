package com.inspark.courseregistrationmodule.service;

import com.inspark.courseregistrationmodule.domain.Tutor;
import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import com.inspark.courseregistrationmodule.dto.TutorAvailableTimeDto;
import com.inspark.courseregistrationmodule.repository.TutorAvailableTimeRepository;
import com.inspark.courseregistrationmodule.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock TutorRepository tutorRepository;
    @Mock TutorAvailableTimeRepository tutorAvailableTimeRepository;
    @InjectMocks TutorService tutorService;

    @Test
    void addTutor_success() {
        Tutor tutor = new Tutor();
        tutor.setEmail("test@tutor.com");

        when(tutorRepository.existsByEmail(tutor.getEmail())).thenReturn(false);

        tutorService.addTutor(tutor);

        verify(tutorRepository).save(tutor);
    }

    @Test
    void addTutor_throws_when_email_exists() {
        Tutor tutor = new Tutor();
        tutor.setEmail("duplicate@tutor.com");

        when(tutorRepository.existsByEmail(tutor.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> tutorService.addTutor(tutor));
    }

    @Test
    void addAvailableTimes_success() {
        String tutorEmail = "tutor@example.com";
        ZonedDateTime now = ZonedDateTime.now();
        TutorAvailableTimeDto dto = new TutorAvailableTimeDto(tutorEmail, List.of(now));

        Tutor tutor = new Tutor();
        tutor.setEmail(tutorEmail);

        when(tutorRepository.findByEmail(tutorEmail)).thenReturn(Optional.of(tutor));

        assertDoesNotThrow(() -> tutorService.addAvailableTimes(dto));
        verify(tutorAvailableTimeRepository).save(any(TutorAvailableTime.class));
    }

    @Test
    void addAvailableTimes_throws_when_time_exists() {
        String tutorEmail = "tutor@example.com";
        ZonedDateTime now = ZonedDateTime.now();
        TutorAvailableTimeDto dto = new TutorAvailableTimeDto(tutorEmail, List.of(now));

        Tutor tutor = new Tutor();
        tutor.setEmail(tutorEmail);

        assertThrows(RuntimeException.class, () -> tutorService.addAvailableTimes(dto));
    }

    @Test
    void removeAvailableTimes_success() {
        String tutorEmail = "tutor@example.com";
        ZonedDateTime now = ZonedDateTime.now();
        TutorAvailableTimeDto dto = new TutorAvailableTimeDto(tutorEmail, List.of(now));

        Tutor tutor = new Tutor();
        tutor.setEmail(tutorEmail);

        when(tutorAvailableTimeRepository.existsByTutorEmailAndAvailableTime(tutorEmail, now)).thenReturn(true);
        assertDoesNotThrow(() -> tutorService.removeAvailableTimes(dto));
        verify(tutorAvailableTimeRepository).deleteByTutorEmailAndAvailableTime(tutorEmail, now);
    }

    @Test
    void removeAvailableTimes_throws_when_time_not_exists() {
        String tutorEmail = "tutor@example.com";
        ZonedDateTime now = ZonedDateTime.now();
        TutorAvailableTimeDto dto = new TutorAvailableTimeDto(tutorEmail, List.of(now));

        Tutor tutor = new Tutor();
        tutor.setEmail(tutorEmail);

        assertThrows(RuntimeException.class, () -> tutorService.removeAvailableTimes(dto));
    }

    @Test
    void addAvailableTimes_partialOverlap_throws() {
        String tutorEmail = "tutor@example.com";
        ZonedDateTime t1 = ZonedDateTime.now().withMinute(0);
        ZonedDateTime t2 = t1.plusMinutes(30);
        ZonedDateTime t3 = t1.plusMinutes(60);

        TutorAvailableTimeDto dto = new TutorAvailableTimeDto(tutorEmail, List.of(t1, t2, t3));

        Tutor tutor = new Tutor();
        tutor.setEmail(tutorEmail);

        when(tutorRepository.findByEmail(tutorEmail)).thenReturn(Optional.of(tutor));
        when(tutorAvailableTimeRepository.existsByTutorEmailAndAvailableTime(tutorEmail, t2)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> tutorService.addAvailableTimes(dto));
    }

    @Test
    void removeAvailableTimes_partialMissing_throws() {
        String tutorEmail = "tutor@example.com";
        ZonedDateTime t1 = ZonedDateTime.now();
        ZonedDateTime t2 = t1.plusMinutes(30);

        TutorAvailableTimeDto dto = new TutorAvailableTimeDto(tutorEmail, List.of(t1, t2));

        Tutor tutor = new Tutor();
        tutor.setEmail(tutorEmail);

        when(tutorRepository.findByEmail(tutorEmail)).thenReturn(Optional.of(tutor));
        when(tutorAvailableTimeRepository.existsByTutorEmailAndAvailableTime(tutorEmail, t2)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> tutorService.removeAvailableTimes(dto));
    }

    @Test
    void addAvailableTimes_multipleTimes_success() {
        String tutorEmail = "tutor@example.com";
        Tutor tutor = new Tutor();
        tutor.setEmail(tutorEmail);

        ZonedDateTime t1 = ZonedDateTime.now().withMinute(0);
        ZonedDateTime t2 = t1.plusMinutes(30);
        ZonedDateTime t3 = t1.plusMinutes(60);

        TutorAvailableTimeDto dto = new TutorAvailableTimeDto(tutorEmail, List.of(t1, t2, t3));

        when(tutorRepository.findByEmail(tutorEmail)).thenReturn(Optional.of(tutor));
        when(tutorAvailableTimeRepository.existsByTutorEmailAndAvailableTime(eq(tutorEmail), any())).thenReturn(false);

        assertDoesNotThrow(() -> tutorService.addAvailableTimes(dto));
        verify(tutorAvailableTimeRepository, times(3)).save(any());
    }
}