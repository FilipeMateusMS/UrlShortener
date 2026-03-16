package com.filipe.url.shortener.handler;

import com.filipe.url.shortener.exception.UrlNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger( GlobalExceptionHandler.class );

    // Trata Url não encontrada
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(UrlNotFoundException ex, HttpServletRequest request) {
        log.info( "Recurso não encontrado");
        return buildResponseDefault(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request);
    }

    // Quando um endpoint não é encontrado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn( "Rota não encontrada");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Método não suportado: " + ex.getMethod());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        log.warn( "Endpoint não encontrado");
        return buildResponseDefault(HttpStatus.NOT_FOUND, "Endpoint não encontrado", ex.getMessage(), request);
    }

    // Erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleSuperiorException(Exception ex, HttpServletRequest request) {
        log.error( "Erro={}", ex.getMessage() );
        log.error( "StackTrace={}", (Object) ex.getStackTrace());
        return buildResponseDefault(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado no servidor",
                request );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldMessage> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add( new FieldMessage( error.getField(), error.getDefaultMessage() ) )
        );
        return buildResponseField(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação",
                "Um ou mais campos estão inválidos",
                request,
                errors );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpServletRequest request) {
        List<FieldMessage> errors = new ArrayList<>();

        ex.getParameterValidationResults().forEach(result -> {
            String parameterName = result.getMethodParameter().getParameterName();
            result.getResolvableErrors().forEach(error -> {
                errors.add(new FieldMessage(parameterName, error.getDefaultMessage()));
            });
        });

        return buildResponseField(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação",
                "Parâmetros de requisição inválidos",
                request,
                errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(BindException ex, HttpServletRequest request) {
        List<FieldMessage> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(new FieldMessage(error.getField(), error.getDefaultMessage()))
        );

        return buildResponseField(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação de Formulário",
                "Um ou mais campos do formulário estão inválidos",
                request,
                errors);
    }

    @ExceptionHandler( AccessDeniedException.class )
    public ResponseEntity<ErrorResponseDTO> handleAcessDeniedException( AccessDeniedException ex, HttpServletRequest request ){
        return buildResponseDefault(
                HttpStatus.FORBIDDEN,
                "Erro de acesso",
                "Usuário pode não ter permissão para essa ação",
                request );
    }

    // Método para retorna a resposta sem campos nulos
    private ResponseEntity<ErrorResponseDTO> buildResponseDefault(HttpStatus status, String error, String message, HttpServletRequest request) {
        ErrorResponseDTO err = new ErrorResponseDTO(
                LocalDateTime.now( ZoneOffset.UTC ),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status( status ).body( err );
    }

    private ResponseEntity<ErrorResponseDTO> buildResponseField(HttpStatus status, String error, String message, HttpServletRequest request, List<FieldMessage> errors) {
        ErrorResponseDTO err = new ErrorResponseDTO(
                LocalDateTime.now( ZoneOffset.UTC ),
                status.value(),
                error,
                message,
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.status( status ).body( err );
    }
}