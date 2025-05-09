package com.inspark.courseregistrationmodule.dto;

import java.util.List;

public record AvailableTutorDto(
        Long tutorId,
        List<Long> availableTimeBlockIds
) {}