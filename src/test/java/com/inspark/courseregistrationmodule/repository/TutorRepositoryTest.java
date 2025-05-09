package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Tutor;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
})
public class TutorRepositoryTest {

    @Autowired
    private TutorRepository tutorRepository;

    @Test
    void testFindByIdIn() {
        // given
        Tutor tutor1 = tutorRepository.save(Tutor.builder()
                .name("Alice")
                .email("alice@example.com")
                .build());

        Tutor tutor2 = tutorRepository.save(Tutor.builder()
                .name("Bob")
                .email("bob@example.com")
                .build());

        // when
        List<Tutor> foundTutors = tutorRepository.findByIdIn(List.of(tutor1.getId(), tutor2.getId()));

        // then
        assertThat(foundTutors).hasSize(2);
        assertThat(foundTutors).extracting(Tutor::getEmail)
                .containsExactlyInAnyOrder("alice@example.com", "bob@example.com");
    }
}