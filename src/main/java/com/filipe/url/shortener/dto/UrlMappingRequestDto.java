package com.filipe.url.shortener.dto;

import jakarta.validation.constraints.NotEmpty;

public record UrlMappingRequestDto(
    @NotEmpty(message = "URL is required")
    String url ){}
