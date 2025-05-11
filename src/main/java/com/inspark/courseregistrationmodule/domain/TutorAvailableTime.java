package com.inspark.courseregistrationmodule.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(
        name = "tutor_available_times",
        indexes = { // 튜터가 많아지더라도 검색을 용이하게 하기 위한 인덱싱.
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

    // 시간 time zone 30분 단위로 기록될 것.
    private ZonedDateTime availableTime; // UTC 시간

}