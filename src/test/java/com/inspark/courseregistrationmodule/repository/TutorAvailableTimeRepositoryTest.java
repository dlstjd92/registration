package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Tutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TutorAvailableTimeRepositoryTest {

    @Autowired
    TutorAvailableTimeRepository tutorAvailableTimeRepository;

    @Test
    void findAvailableTimes() {
        // given
        ZonedDateTime start = ZonedDateTime.now().withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime end = start.plusHours(1);

        // when
        List<ZonedDateTime> result = tutorAvailableTimeRepository.findAvailableTimes(start, end);

        // then
        assertNotNull(result);
        // 예시: assertTrue(result.contains(start));
    }

    @Test
    void findTutorsWithTime() {
        // given
        ZonedDateTime start = ZonedDateTime.now().withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime end = start.plusMinutes(30);
        int classLength = 1;

        // when
        List<Tutor> result = tutorAvailableTimeRepository.findTutorsWithTime(start, end, classLength);

        // then
        assertNotNull(result);
        // 예시: assertTrue(result.stream().anyMatch(t -> t.getName().equals("홍길동")));
    }
}