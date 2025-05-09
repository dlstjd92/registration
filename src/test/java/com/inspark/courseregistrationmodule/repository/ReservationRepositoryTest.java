package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
})
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EntityManager em;

    @Test
    void testFindByTutorIdAndDateAndTimeBlockId() {
        Tutor tutor = tutorRepository.save(Tutor.builder().name("Test").email("t1@test.com").build());
        Student student = studentRepository.save(Student.builder().name("Student").email("s1@test.com").build());
        TimeBlock block = em.find(TimeBlock.class, 1L);
        LocalDate date = LocalDate.now();

        Reservation reservation = Reservation.builder()
                .tutor(tutor)
                .student(student)
                .date(date)
                .timeBlock(block)
                .status(Reservation.ReservationStatus.RESERVED)
                .build();
        reservation = reservationRepository.save(reservation);

        Optional<Reservation> found = reservationRepository.findByTutorIdAndDateAndTimeBlockId(tutor.getId(), date, block.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(reservation.getId());
    }

    @Test
    void testExistsByTutorIdAndDateAndTimeBlockId() {
        Tutor tutor = tutorRepository.save(Tutor.builder().name("T").email("t2@test.com").build());
        Student student = studentRepository.save(Student.builder().name("S").email("s2@test.com").build());
        TimeBlock block = em.find(TimeBlock.class, 2L);
        LocalDate date = LocalDate.now();

        Reservation reservation = Reservation.builder()
                .tutor(tutor)
                .student(student)
                .date(date)
                .timeBlock(block)
                .status(Reservation.ReservationStatus.RESERVED)
                .build();
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.existsByTutorIdAndDateAndTimeBlockId(tutor.getId(), date, block.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByStudentIdAndDateBetween() {
        Tutor tutor = tutorRepository.save(Tutor.builder().name("T3").email("t3@test.com").build());
        Student student = studentRepository.save(Student.builder().name("S3").email("s3@test.com").build());
        TimeBlock block = em.find(TimeBlock.class, 3L);
        LocalDate date = LocalDate.of(2025, 5, 10);

        Reservation r1 = Reservation.builder()
                .tutor(tutor)
                .student(student)
                .date(date)
                .timeBlock(block)
                .status(Reservation.ReservationStatus.RESERVED)
                .build();
        reservationRepository.save(r1);

        Reservation r2 = Reservation.builder()
                .tutor(tutor)
                .student(student)
                .date(date.plusDays(1))
                .timeBlock(block)
                .status(Reservation.ReservationStatus.RESERVED)
                .build();
        reservationRepository.save(r2);

        List<Reservation> found = reservationRepository.findByStudentIdAndDateBetween(
                student.getId(), date.minusDays(1), date.plusDays(2));

        assertThat(found).hasSize(2);
    }
}