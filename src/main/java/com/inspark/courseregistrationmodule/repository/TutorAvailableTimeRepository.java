package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorAvailableTimeRepository extends JpaRepository<TutorAvailableTime, Long> {

    // 특정 튜터의 전체 가능 시간대 조회
    List<TutorAvailableTime> findByTutorId(Long tutorId);

    // 시간대에 가능한 튜터 ID 조회 (기본 조회)
    List<TutorAvailableTime> findByTimeBlockId(Long timeBlockId);

    // TODO 새로 추가한 메소드들 테스트 만들어야함
    void deleteByTutorIdAndTimeBlockId(Long tutorId, Long timeBlockId);

    void deleteByTutorIdAndTimeBlockIdIn(Long tutorId, List<Long> blockIds);

    // 해당 튜터의 시간이 존재하는지 여부 확인
    boolean existsByTutorIdAndTimeBlockId(Long tutorId, Long timeBlockId);
}