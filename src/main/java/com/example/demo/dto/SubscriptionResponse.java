package com.example.demo.dto;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(UUID id, UUID userId, UUID courseId, Instant subscribedAt) {}
