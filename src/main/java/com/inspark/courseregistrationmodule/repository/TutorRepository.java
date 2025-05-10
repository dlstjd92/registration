package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    boolean existsByEmail(String email);

}