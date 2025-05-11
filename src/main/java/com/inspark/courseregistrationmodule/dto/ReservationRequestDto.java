package com.inspark.courseregistrationmodule.dto;

import java.time.ZonedDateTime;

public record ReservationRequestDto(
        Long studentId,
        Long tutorId,
        ZonedDateTime startDate,
        int classLength
) {}
