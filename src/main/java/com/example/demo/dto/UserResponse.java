package com.example.demo.dto;

import java.util.UUID;

public record UserResponse(
    UUID id, String firstName, String lastName, String userName, String email) {}
