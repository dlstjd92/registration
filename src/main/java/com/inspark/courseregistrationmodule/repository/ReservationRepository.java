package com.inspark.courseregistrationmodule.repository;

import com.inspark.courseregistrationmodule.domain.Reservation;
import com.inspark.courseregistrationmodule.dto.ReservationResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation, Long> {

    boolean existsByTutor_EmailAndStartDate(String email, ZonedDateTime startDate);

    @Query("""
    SELECT new com.inspark.courseregistrationmodule.dto.ReservationResponseDto(
        MIN(r.id), MIN(r.startDate), r.tutor.name, r.lessonGroupId, r.classLength
    )
    FROM Reservation r
    WHERE r.student.email = :email
    GROUP BY r.lessonGroupId, r.tutor.name, r.classLength
""")
    List<ReservationResponseDto> findAllByStudent_email(@Param("email") String email);

}
