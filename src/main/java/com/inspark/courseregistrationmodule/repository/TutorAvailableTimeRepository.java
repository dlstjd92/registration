package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Tutor;
import com.inspark.courseregistrationmodule.domain.TutorAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TutorAvailableTimeRepository extends JpaRepository<TutorAvailableTime, Long> {

    // 해당 튜터의 시간이 존재하는지 여부 확인
    boolean existsByTutorEmailAndAvailableTime(String tutorEmail, ZonedDateTime availableTime);

    void deleteByTutorEmailAndAvailableTime(String tutorEmail, ZonedDateTime availableTime);
    // 여러 tutorId 목록으로 튜터들을 한 번에 조회

    @Query("""
    SELECT DISTINCT t.availableTime
    FROM TutorAvailableTime t
    WHERE t.availableTime BETWEEN :startTime AND :endTime
      AND t.availableTime NOT IN (
          SELECT r.startDate
          FROM Reservation r
          WHERE r.startDate BETWEEN :startTime AND :endTime
      )
    order by t.availableTime asc
    """)
    List<ZonedDateTime> findAvailableTimes(
            @Param("startTime") ZonedDateTime startTime,
            @Param("endTime") ZonedDateTime endTime
    );

    @Query("""
SELECT t
FROM TutorAvailableTime a
JOIN a.tutor t
WHERE a.availableTime BETWEEN :startTime AND :endClassTime
  AND a.availableTime NOT IN (
      SELECT r.startDate
      FROM Reservation r
      WHERE r.startDate BETWEEN :startTime AND :endClassTime
  )
GROUP BY t.id
HAVING COUNT(a) = :classLength
""")
    List<Tutor> findTutorsWithTime(
            @Param("startTime") ZonedDateTime startTime,
            @Param("endClassTime") ZonedDateTime endClassTime,
            @Param(("classLength")) int classLength
    );
}