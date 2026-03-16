package com.filipe.url.shortener.service;


import com.filipe.url.shortener.dto.UrlMappingRequestDto;
import com.filipe.url.shortener.dto.UrlMappingResponseDto;
import com.filipe.url.shortener.exception.UrlNotFoundException;
import com.filipe.url.shortener.mappers.UrlMappingMapper;
import com.filipe.url.shortener.model.UrlMapping;
import com.filipe.url.shortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlMappingRepository repository;

    @Autowired
    private UrlMappingMapper mapper;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 6;

    public UrlMappingResponseDto createShortUrl(String originalUrl) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortCode( generateUniqueShortCode() );
        urlMapping.setUrl( originalUrl );
        return mapper.toDto(repository.save( urlMapping ) );
    }

    public UrlMappingResponseDto getOriginalUrl(String shortCode) {
        UrlMapping urlMapping = repository.findById(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("ShortCode: '" + shortCode + "' não encontrado"));

        urlMapping.setAccessCount( urlMapping.getAccessCount() + 1 );
        UrlMapping originalUrl = repository.save(urlMapping);
        return mapper.toDto(originalUrl);
    }

    @Transactional(readOnly = true)
    public UrlMappingResponseDto getUrlStats(String shortCode) {
        UrlMapping urlMapping = repository.findById( shortCode )
                .orElseThrow( () -> new UrlNotFoundException("ShortCode: '" + shortCode + "' não encontrado") );
        return mapper.toDto( urlMapping );
    }

    private String generateUniqueShortCode() {
        Random random = new Random();
        String shortCode;
        int attempts = 0;
        do {
            StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
            for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            shortCode = sb.toString();
            attempts++;
            if (attempts > 10) {
                throw new RuntimeException("Failed to generate a unique short code after 10 attempts");
            }
        } while (repository.existsById(shortCode));
        return shortCode;
    }

    @Transactional
    public UrlMappingResponseDto updateShortUrl(String shortCode, UrlMappingRequestDto urlMappingRequestDto ) {
        UrlMapping urlMapping = repository.findById(shortCode)
                .orElseThrow( () -> new UrlNotFoundException("ShortCode: '" + shortCode + "' não encontrado") );
        urlMapping.setUrl( urlMappingRequestDto.url() );
        return mapper.toDto( repository.save( urlMapping ) );
    }

    @Transactional
    public void deleteShortUrl(String shortCode) {
        if (!repository.existsById(shortCode))
            throw new UrlNotFoundException("ShortCode: '" + shortCode + "' não encontrado");
        repository.deleteById(shortCode);
    }
}