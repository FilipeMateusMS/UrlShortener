package com.filipe.url.shortener.mappers;

import com.filipe.url.shortener.dto.UrlMappingResponseDto;
import com.filipe.url.shortener.model.UrlMapping;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring" )
public interface UrlMappingMapper {
    UrlMappingResponseDto toDto( UrlMapping urlMapping );
}
