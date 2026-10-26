package com.example.demo.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
