package com.filipe.url.shortener.dto;

import java.time.LocalDateTime;

public record UrlMappingResponseDto(
        String shortCode,
        String url,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String accessCount ) {}
