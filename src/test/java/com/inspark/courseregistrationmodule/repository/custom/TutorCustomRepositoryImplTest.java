package com.inspark.courseregistrationmodule.repository.custom;

import com.inspark.courseregistrationmodule.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
})
class TutorCustomRepositoryImplTest {

    @Autowired
    EntityManager em;

    TutorCustomRepositoryImpl customRepository;

    @BeforeEach
    void setUp() {
        customRepository = new TutorCustomRepositoryImpl(em);
    }

    @Test
    void date_twoBlock_test() {
        // given
        Tutor tutor = Tutor.builder()
                .name("Jane")
                .email("jane@example.com")
                .build();
        em.persist(tutor);

        TimeBlock block1 = em.createQuery("SELECT t FROM TimeBlock t WHERE t.label = :label", TimeBlock.class)
                .setParameter("label", "10:00")
                .getSingleResult();

        TimeBlock block2 = em.createQuery("SELECT t FROM TimeBlock t WHERE t.label = :label", TimeBlock.class)
                .setParameter("label", "10:30")
                .getSingleResult();
        em.persist(block1);
        em.persist(block2);

        TutorAvailableTime tat1 = TutorAvailableTime.builder()
                .tutor(tutor)
//                .dayOfWeek(DayOfWeek.FRIDAY)
                .timeBlock(block1)
                .build();

        TutorAvailableTime tat2 = TutorAvailableTime.builder()
                .tutor(tutor)
//                .dayOfWeek(DayOfWeek.FRIDAY)
                .timeBlock(block2)
                .build();

        em.persist(tat1);
        em.persist(tat2);

        LocalDate date = LocalDate.of(2025, 5, 9); // 금요일

//        List<?> list = em.createNativeQuery("SELECT day_of_week FROM tutor_available_times").getResultList();

        // when
        Map<LocalDate, List<Long>> result = customRepository.findAvailableTimeBlocks(date, date, 2);

        // then
        assertThat(result).containsKey(date);
        List<Long> blocks = result.get(date);

        System.out.println("block1 ID: " + block1.getId());
        System.out.println("block2 ID: " + block2.getId());
        System.out.println("Result block list = " + blocks);

        assertThat(blocks).contains(block1.getId());
    }

    private Tutor createTutor(String email) {
        return Tutor.builder()
                .name("Test Tutor")
                .email(email)
                .build();
    }

    private Student createStudent(String email) {
        return Student.builder()
                .name("Test Student")
                .email(email)
                .build();
    }

    private TimeBlock createTimeBlock(String start, String end) {
        return TimeBlock.builder()
                .label(start + "-" + end)
                .startTime(start)
                .endTime(end)
                .build();
    }

    @Test
    void testFindAvailableTutorIds_successfulMatch() {
        // given
        Tutor tutor1 = createTutor("tutor1@email.com");
        Tutor tutor2 = createTutor("tutor2@email.com");
        em.persist(tutor1);
        em.persist(tutor2);

        Student student = createStudent("student@email.com");
        em.persist(student);

        TimeBlock block1 = createTimeBlock("09:00", "09:30");
        TimeBlock block2 = createTimeBlock("09:30", "10:00");
        em.persist(block1);
        em.persist(block2);

        DayOfWeek testDay = LocalDate.of(2025, 5, 12).getDayOfWeek();

        em.persist(TutorAvailableTime.builder().tutor(tutor1).timeBlock(block1).build());
        em.persist(TutorAvailableTime.builder().tutor(tutor1).timeBlock(block2).build());
        em.persist(TutorAvailableTime.builder().tutor(tutor2).timeBlock(block1).build());
        em.persist(TutorAvailableTime.builder().tutor(tutor2).timeBlock(block2).build());

        Reservation reservation = Reservation.builder()
                .tutor(tutor2)
                .student(student)
                .date(LocalDate.of(2025, 5, 12))
                .timeBlock(block2)
                .status(Reservation.ReservationStatus.RESERVED)
                .build();
        em.persist(reservation);

        // when
        List<Long> availableTutorIds = customRepository.findAvailableTutorIds(
                LocalDate.of(2025, 5, 12),
                List.of(block1.getId(), block2.getId())
        );

        // then
        System.out.println("Available tutor IDs: " + availableTutorIds);
        System.out.println("Expected tutor ID (tutor1): " + tutor1.getId());
        System.out.println("Unexpected tutor ID (tutor2): " + tutor2.getId());

        Set<Long> expectedIds = Set.of(tutor1.getId());
        System.out.println("Match: " + expectedIds.equals(Set.copyOf(availableTutorIds)));
        assertThat(availableTutorIds).containsExactly(tutor1.getId());
    }


}