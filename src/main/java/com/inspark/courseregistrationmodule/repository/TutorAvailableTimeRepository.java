package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface TutorAvailableTimeRepository extends JpaRepository<TutorAvailableTime, Long> {

    // 특정 튜터의 전체 가능 시간대 조회
    List<TutorAvailableTime> findByTutorId(Long tutorId);

    // 특정 요일 + 시간대에 가능한 튜터 ID 조회 (기본 조회)
    List<TutorAvailableTime> findByDayOfWeekAndTimeBlockId(DayOfWeek dayOfWeek, Long timeBlockId);
}