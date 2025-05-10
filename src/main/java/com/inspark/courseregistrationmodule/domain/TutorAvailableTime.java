package com.inspark.courseregistrationmodule.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;

@Entity
@Table(
        name = "tutor_available_times",
        indexes = { // 튜터가 많아지더라도 검색을 용이하게 하기 위한 인덱싱.
        @Index(name = "idx_day_time_tutor", columnList = "day_of_week, time_block_id, tutor_id"), // 학생 검색용
        @Index(name = "idx_tutor_only", columnList = "tutor_id") // 튜터 시간 검색용
            }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorAvailableTime {

    // 튜터가 설정한 수업 가능한 시간

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor; // 튜터

    // 시간 블록 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_block_id", nullable = false)
    private TimeBlock timeBlock; // 가능한 시간 블록

    // 묵시적으로 해당하지 않는 시간블록은 수업불가 시간임
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "day_of_week", nullable = false)
//    private DayOfWeek dayOfWeek; // 요일 ( 확장의 여지 )
}