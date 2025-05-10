package com.inspark.courseregistrationmodule.repository.custom;

import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TutorCustomRepository {
    // 요구사항에 맞는 커스텀 쿼리문 -> 특정 시간대, 수업길이로 수업 가능한 튜터 조회
    @Query("SELECT tat.id" +
            "            FROM TutorAvailableTime tat" +
            "            WHERE tat.timeBlock.id IN :timeBlockIds" +
            "            GROUP BY tat.tutor.id" +
            "            HAVING COUNT(*) = :requiredBlockCount" +
            "              AND tat.tutor.id NOT IN (" +
            "                  SELECT r.tutor.id" +
            "                  FROM Reservation r" +
            "                  WHERE r.date = :date" +
            "                    AND r.timeBlock.id IN :timeBlockIds)")
    List<Long> findAvailableTutorIds(LocalDate date, List<Long> timeBlockIds);


    // 기간, 수업길이로 수업가능한 시간대 보기
    public Map<LocalDate, List<Long>> findAvailableTimeBlocks(LocalDate from, LocalDate to, int sessionLength);
}