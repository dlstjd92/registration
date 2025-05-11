package com.inspark.courseregistrationmodule.service;

import com.inspark.courseregistrationmodule.domain.*;
import com.inspark.courseregistrationmodule.dto.ReservationRequestDto;
import com.inspark.courseregistrationmodule.dto.ReservationResponseDto;
import com.inspark.courseregistrationmodule.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private TutorRepository tutorRepository;
    @Mock private ReservationRepository reservationRepository;
    @Mock private TutorAvailableTimeRepository tutorAvailableTimeRepository;

    @InjectMocks private StudentService studentService;

    private static final int TIME_BLOCK_MINUTES = 30;

    @Test
    void addStudent_success() {
        Student student = new Student();
        student.setEmail("test@example.com");

        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);

        studentService.addStudent(student);

        verify(studentRepository).save(student);
    }

    @Test
    void addStudent_duplicateEmail_throws() {
        Student student = new Student();
        student.setEmail("test@example.com");

        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                studentService.addStudent(student));

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void findStartTimesWithDurationAndClassLength_returnsAvailableTimes() {
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusHours(1);

        List<ZonedDateTime> available = List.of(
                start,
                start.plusMinutes(30),
                start.plusMinutes(60)
        );

        when(tutorAvailableTimeRepository.findAvailableTimes(start, end)).thenReturn(available);

        List<ZonedDateTime> result = studentService.findStartTimesWithDurationAndClassLength(start, end, 2);

        assertEquals(2, result.size()); // start, start+30 -> valid
    }

    @Test
    void findTutorWithTimeAndClassLength_returnsTutors() {
        ZonedDateTime time = ZonedDateTime.now();
        int classLength = 2;

        List<Tutor> tutors = List.of(new Tutor(), new Tutor());
        when(tutorAvailableTimeRepository.findTutorsWithTime(any(), any(), eq(classLength)))
                .thenReturn(tutors);

        List<Tutor> result = studentService.findTutorWithTimeAndClassLength(time, classLength);

        assertEquals(2, result.size());
    }

    @Test
    void makeReservation_success() {
        ZonedDateTime start = ZonedDateTime.now();
        int classLength = 2;

        Student student = Student.builder().email("student@test.com").build();
        Tutor tutor = Tutor.builder().email("tutor@test.com").build();

        ReservationRequestDto dto = new ReservationRequestDto(
                "student@test.com",
                "tutor@test.com",
                start,
                classLength
        );

        when(studentRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));
        when(tutorRepository.findByEmail("tutor@test.com")).thenReturn(Optional.of(tutor));
        when(tutorAvailableTimeRepository.existsByTutorEmailAndAvailableTime(eq(tutor.getEmail()), any())).thenReturn(true);
        when(reservationRepository.existsByTutor_EmailAndStartDate(eq("tutor@test.com"), any())).thenReturn(false);

        assertDoesNotThrow(() -> studentService.makeReservation(dto));
        verify(reservationRepository, times(2)).save(any());
    }

    @Test
    void makeReservation_conflict_throws() {
        ZonedDateTime start = ZonedDateTime.now();
        int classLength = 2;

        Student student = Student.builder().email("student@test.com").build();
        Tutor tutor = Tutor.builder().email("tutor@test.com").build();

        ReservationRequestDto dto = new ReservationRequestDto(
                "student@test.com",
                "tutor@test.com",
                start,
                classLength
        );

        when(studentRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));
        when(tutorRepository.findByEmail("tutor@test.com")).thenReturn(Optional.of(tutor));
        when(tutorAvailableTimeRepository.existsByTutorEmailAndAvailableTime(eq(tutor.getEmail()), any())).thenReturn(true);
        when(reservationRepository.existsByTutor_EmailAndStartDate(eq("tutor@test.com"), any()))
                .thenReturn(true); // simulate conflict

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                studentService.makeReservation(dto));

        assertTrue(ex.getMessage().contains("이미 예약이 존재"));
    }

    @Test
    void getReservationsByStudent_returnsList() {
        List<ReservationResponseDto> reservations = List.of(
                new ReservationResponseDto(1L, ZonedDateTime.now(), "Tutor A", "2025-05-10T09:00:00Z_2", 2),
                new ReservationResponseDto(2L, ZonedDateTime.now().plusMinutes(30), "Tutor A", "2025-05-10T09:00:00Z_2", 2)
        );

        when(reservationRepository.findAllByStudent_email("test@example.com")).thenReturn(reservations);

        var result = studentService.getReservationsByStudent("test@example.com");

        assertEquals(2, result.size());
    }

    @Test
    void addStudent_throws_when_email_exists() {
        Student student = new Student();
        student.setEmail("dup@student.com");

        when(studentRepository.existsByEmail("dup@student.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> studentService.addStudent(student));
    }

    @Test
    void addStudent_throws_when_null() {
        assertThrows(RuntimeException.class, () -> studentService.addStudent(null));
    }

    @Test
    void findStartTimes_throws_when_start_or_end_null() {
        assertThrows(RuntimeException.class, () ->
                studentService.findStartTimesWithDurationAndClassLength(null, ZonedDateTime.now(), 1));
        assertThrows(RuntimeException.class, () ->
                studentService.findStartTimesWithDurationAndClassLength(ZonedDateTime.now(), null, 1));
    }

    @Test
    void findStartTimes_throws_when_invalid_classLength() {
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusHours(1);

        assertThrows(RuntimeException.class, () ->
                studentService.findStartTimesWithDurationAndClassLength(start, end, 0));
        assertThrows(RuntimeException.class, () ->
                studentService.findStartTimesWithDurationAndClassLength(start, end, 10));
    }

    @Test
    void findStartTimes_returns_empty_when_no_contiguous_block() {
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusHours(2);

        List<ZonedDateTime> times = List.of(
                start,
                start.plusMinutes(15),
                start.plusMinutes(50)
        );

        when(tutorAvailableTimeRepository.findAvailableTimes(start, end)).thenReturn(times);

        List<ZonedDateTime> result = studentService.findStartTimesWithDurationAndClassLength(start, end, 2);
        assertTrue(result.isEmpty());
    }

    @Test
    void makeReservation_throws_when_classLength_invalid() {
        ReservationRequestDto dto = new ReservationRequestDto(
                "student@test.com",
                "tutor@test.com",
                ZonedDateTime.now(),
                0
        );

        assertThrows(RuntimeException.class, () -> studentService.makeReservation(dto));
    }

    @Test
    void makeReservation_throws_when_tutor_not_found() {
        ZonedDateTime start = ZonedDateTime.now();
        ReservationRequestDto dto = new ReservationRequestDto(
                "student@test.com",
                "notfound@tutor.com",
                start,
                1
        );

        when(tutorRepository.findByEmail("notfound@tutor.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.makeReservation(dto));
    }

    @Test
    void makeReservation_throws_when_student_not_found() {
        ZonedDateTime start = ZonedDateTime.now();
        ReservationRequestDto dto = new ReservationRequestDto(
                "notfound@student.com",
                "tutor@test.com",
                start,
                1
        );

        when(tutorRepository.findByEmail("tutor@test.com")).thenReturn(Optional.of(new Tutor()));
        when(studentRepository.findByEmail("notfound@student.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.makeReservation(dto));
    }
}