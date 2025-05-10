package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {
    // 기본적인 findById, findAll 등을 JpaRepository가 자동 제공
}