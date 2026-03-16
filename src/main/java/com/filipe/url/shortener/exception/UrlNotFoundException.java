package com.filipe.url.shortener.exception;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String message ){
        super( message );
    }
}
