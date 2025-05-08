package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 중복 예약 체크 (튜터 + 날짜 + 시간대 조합)
    Optional<Reservation> findByTutorIdAndDateAndTimeBlockId(Long tutorId, LocalDate date, Long timeBlockId);

    // 해당 예약이 존재하는지 (중복 방지용 빠른 체크)
    boolean existsByTutorIdAndDateAndTimeBlockId(Long tutorId, LocalDate date, Long timeBlockId);

    // 특정 학생의 예약 내역
    List<Reservation> findByStudentIdAndDateBetween(Long studentId, LocalDate start, LocalDate end);
} // TODO 각 쿼리문별로 필요한지 면밀히 체크하고 계획