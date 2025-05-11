package com.inspark.courseregistrationmodule.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record TutorAvailableTimeDto(
        String tutorEmail,
        List<ZonedDateTime> times
) {}