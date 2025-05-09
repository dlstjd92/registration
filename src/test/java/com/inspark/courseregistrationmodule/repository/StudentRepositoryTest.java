package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Student;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
})
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testFindByEmail() {
        // given
        Student student = Student.builder()
                .name("Jane")
                .email("jane@example.com")
                .build();
        studentRepository.save(student);

        // when
        Optional<Student> found = studentRepository.findByEmail("jane@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jane");
    }
}