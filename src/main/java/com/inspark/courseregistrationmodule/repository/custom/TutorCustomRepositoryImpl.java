package com.inspark.courseregistrationmodule.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class TutorCustomRepositoryImpl implements TutorCustomRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Long> findAvailableTutorIds(LocalDate date, List<Long> timeBlockIds) {
        String sql = """
            SELECT tat.tutor_id
            FROM tutor_available_times tat
            WHERE tat.time_block_id IN :timeBlockIds
            GROUP BY tat.tutor_id
            HAVING COUNT(*) = :requiredBlockCount
              AND tat.tutor_id NOT IN (
                  SELECT r.tutor_id
                  FROM reservations r
                  WHERE r.date = :date
                    AND r.time_block_id IN :timeBlockIds
              )
        """;

        return em.createNativeQuery(sql)
//                .setParameter("dayOfWeek", date.getDayOfWeek().name()) // 1=Monday
                .setParameter("timeBlockIds", timeBlockIds)
                .setParameter("requiredBlockCount", timeBlockIds.size())
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public Map<LocalDate, List<Long>> findAvailableTimeBlocks(LocalDate from, LocalDate to, int sessionLength) {
        Map<LocalDate, List<Long>> result = new LinkedHashMap<>();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            String dayOfWeek = date.getDayOfWeek().name();

            // 가능한 블록
            String sql1 = """
                SELECT tat.time_block_id
                FROM tutor_available_times tat
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM reservations r
                    WHERE r.date = :date
                      AND r.time_block_id = tat.time_block_id
                      AND r.tutor_id = tat.tutor_id
                )
                GROUP BY tat.time_block_id
            """;

            List<Number> blocks = em.createNativeQuery(sql1)
//                    .setParameter("dayOfWeek", dayOfWeek)
                    .setParameter("date", date)
                    .getResultList();

            List<Long> sortedBlocks = blocks.stream()
                    .map(Number::longValue)
                    .sorted()
                    .toList();

            // 세션 길이 반영: 연속된 블록만 필터
            List<Long> validBlocks = new ArrayList<>();
            if (sessionLength == 1) {
                validBlocks = sortedBlocks;
            } else {
                for (int i = 0; i <= sortedBlocks.size() - sessionLength; i++) {
                    boolean continuous = true;
                    for (int j = 0; j < sessionLength - 1; j++) {
                        if (sortedBlocks.get(i + j) + 1 != sortedBlocks.get(i + j + 1)) {
                            continuous = false;
                            break;
                        }
                    }
                    if (continuous) {
                        validBlocks.add(sortedBlocks.get(i));
                    }
                }
            }
            result.put(date, validBlocks);
        }

        return result;
    }


}