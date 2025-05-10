package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TutorAvailableTimeRepository extends JpaRepository<TutorAvailableTime, Long> {

    // TODO 새로 추가한 메소드들 테스트 만들어야함
    // 해당 튜터의 시간이 존재하는지 여부 확인
    boolean existsByTutorIdAndAvailableTime(Long tutorId, ZonedDateTime availableTime);

    void deleteByTutorIdAndAvailableTime(Long tutorId, ZonedDateTime availableTime);
    // 여러 tutorId 목록으로 튜터들을 한 번에 조회
}