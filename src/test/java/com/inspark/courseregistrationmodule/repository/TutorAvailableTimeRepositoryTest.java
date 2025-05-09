package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Tutor;
import com.inspark.courseregistrationmodule.domain.TimeBlock;
import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
})
public class TutorAvailableTimeRepositoryTest {

    @Autowired
    private TutorAvailableTimeRepository repository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void testFindByTutorId() {
        Tutor tutor = Tutor.builder()
                .name("Test Tutor")
                .email("test@example.com")
                .build();
        em.persist(tutor);

        TimeBlock block = em.createQuery("SELECT t FROM TimeBlock t WHERE t.label = :label", TimeBlock.class)
                .setParameter("label", "09:00")
                .getSingleResult();

        TutorAvailableTime tat = TutorAvailableTime.builder()
                .tutor(tutor)
                .dayOfWeek(DayOfWeek.MONDAY)
                .timeBlock(block)
                .build();
        em.persist(tat);

        List<TutorAvailableTime> result = repository.findByTutorId(tutor.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result.get(0).getTimeBlock().getLabel()).isEqualTo("09:00");
    }

    @Test
    void testFindByDayOfWeekAndTimeBlockId() {
        Tutor tutor = Tutor.builder()
                .name("Test Tutor")
                .email("test2@example.com")
                .build();
        em.persist(tutor);

        TimeBlock block = em.createQuery("SELECT t FROM TimeBlock t WHERE t.label = :label", TimeBlock.class)
                .setParameter("label", "10:00")
                .getSingleResult();

        TutorAvailableTime tat = TutorAvailableTime.builder()
                .tutor(tutor)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .timeBlock(block)
                .build();
        em.persist(tat);

        List<TutorAvailableTime> result = repository.findByDayOfWeekAndTimeBlockId(DayOfWeek.FRIDAY, block.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTutor().getEmail()).isEqualTo("test2@example.com");
    }
}