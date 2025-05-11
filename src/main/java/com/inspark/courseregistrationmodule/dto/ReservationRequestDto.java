package com.inspark.courseregistrationmodule.dto;

import java.time.ZonedDateTime;

public record ReservationRequestDto(
        String studentEmail,
        String tutorEmail,
        ZonedDateTime startDate,
        int classLength
) {}
