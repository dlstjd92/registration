package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    // 여러 tutorId 목록으로 튜터들을 한 번에 조회
    List<Tutor> findByIdIn(List<Long> tutorIds);
}