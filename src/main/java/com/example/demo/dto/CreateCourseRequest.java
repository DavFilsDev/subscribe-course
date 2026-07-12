package com.example.demo.dto;

import java.time.Instant;

public record CreateCourseRequest(String title, Instant startDate, Instant endDate) {}
