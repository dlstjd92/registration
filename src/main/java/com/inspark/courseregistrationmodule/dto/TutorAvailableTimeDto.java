package com.inspark.courseregistrationmodule.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record TutorAvailableTimeDto(
        Long tutorId,
        List<ZonedDateTime> times
) {}