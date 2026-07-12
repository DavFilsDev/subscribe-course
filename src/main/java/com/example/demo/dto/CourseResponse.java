package com.example.demo.dto;

import java.time.Instant;
import java.util.UUID;

public record CourseResponse(UUID id, String title, Instant startDate, Instant endDate) {}
