package com.inspark.courseregistrationmodule.dto;

import java.time.ZonedDateTime;


public record ReservationResponseDto(
        Long id,
        ZonedDateTime startDate,
        String tutorName,
        String lessonGroupId,
        int classLength
) {}
