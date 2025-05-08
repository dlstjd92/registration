package com.inspark.courseregistrationmodule.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "reservations",
        indexes = { // 역시나 속도를 위한 인덱싱
                @Index(name = "idx_reservation_lookup", columnList = "tutor_id, date, time_block_id"), // 특정시간대에 예약이 있는지 확인
                @Index(name = "idx_student_reservation", columnList = "student_id, date") // 학생이 자신의 스케줄을 조회할 경우
        },
        uniqueConstraints = { // 다중 요청에도 원자성을 유지하기 위한 유니크제약
                @UniqueConstraint(name = "uk_reservation_unique", columnNames = {"tutor_id", "date", "time_block_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    public enum ReservationStatus { // 상태관리 enum
        RESERVED,
        CANCELED,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // 예약 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; // 예약한 학생

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor; // 예약된 튜터

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_block_id", nullable = false)
    private TimeBlock timeBlock; // 예약한 시간블록

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status; // 상태
}

