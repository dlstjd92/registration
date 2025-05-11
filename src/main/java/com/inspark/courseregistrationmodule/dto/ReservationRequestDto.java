package com.inspark.courseregistrationmodule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ReservationRequestDto {

    private Long studentId;         // 예약할 학생의 ID
    private Long tutorId;           // 수업을 진행할 튜터의 ID
    private ZonedDateTime startDate; // 예약 시작 시간 (30분 단위로 관리)
    private int classLength;

}