package com.filipe.url.shortener.controller;

import com.filipe.url.shortener.dto.UrlMappingRequestDto;
import com.filipe.url.shortener.dto.UrlMappingResponseDto;
import com.filipe.url.shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<UrlMappingResponseDto> createShortUrl(@Valid @RequestBody UrlMappingRequestDto urlMappingDto ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( service.createShortUrl( urlMappingDto.url() ) ) ;
    }

    @GetMapping("/shorten/{shortCode}")
    public ResponseEntity<UrlMappingResponseDto> getOriginalUrl(@PathVariable String shortCode ) {
        return ResponseEntity.ok( service.getOriginalUrl( shortCode ) );
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlMappingResponseDto> redirectToOriginalUrl(@PathVariable String shortCode) {
        UrlMappingResponseDto urlMapping = service.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", urlMapping.url() )
                .build();
    }

    @PutMapping("/shorten/{shortCode}")
    public ResponseEntity<UrlMappingResponseDto> updateShortUrl(@PathVariable String shortCode, @Valid @RequestBody UrlMappingRequestDto urlMappingRequestDto, BindingResult bindingResult) {
        return ResponseEntity.ok( service.updateShortUrl( shortCode, urlMappingRequestDto ) );
    }

    @DeleteMapping("/shorten/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        service.deleteShortUrl( shortCode );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shorten/{shortCode}/stats")
    public ResponseEntity<?> getUrlStats(@PathVariable String shortCode) {
        return ResponseEntity.ok( service.getUrlStats( shortCode ) );
    }
}