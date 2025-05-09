package com.inspark.courseregistrationmodule.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "time_blocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeBlock { // 48개의 타임블록을 정의하는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예: 1 ~ 48

    @Column(nullable = false, unique = true)
    private String label; // 타임블록 이름

    @Column(name = "start_time", nullable = false)
    private String startTime; // 시작시간

    @Column(name = "end_time", nullable = false)
    private String endTime; // 끝시간
}