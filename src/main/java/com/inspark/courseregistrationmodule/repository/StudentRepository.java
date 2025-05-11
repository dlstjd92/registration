package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 이메일 기준 중복 확인 및 단건 조회
    boolean existsByEmail(String email);

    Optional<Student> findByEmail(String email);
}