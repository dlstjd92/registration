package com.inspark.courseregistrationmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {
    private Long id;
    private ZonedDateTime startDate;
    private String tutorName;
    private String lessonGroupId;
    private int classLength;
}
