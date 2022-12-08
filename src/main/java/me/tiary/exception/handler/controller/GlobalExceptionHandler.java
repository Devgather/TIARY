package me.tiary.exception.handler.controller;

import lombok.extern.slf4j.Slf4j;
import me.tiary.exception.AccountException;
import me.tiary.exception.ProfileException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.status.AccountStatus;
import me.tiary.exception.status.ProfileStatus;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final List<String> messages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("Method argument not valid exception occurrence: {}", messages);

        return ResponseEntity.badRequest().body(new ExceptionResponse(messages));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
        final List<String> messages = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        log.warn("Constraint violation exception occurrence: {}", messages);

        return ResponseEntity.badRequest().body(new ExceptionResponse(messages));
    }

    @ExceptionHandler({AccountException.class})
    public ResponseEntity<Object> handleAccountException(final AccountException ex) {
        final AccountStatus status = ex.getStatus();

        log.warn("Account exception occurrence: {}", status.getMessage());

        return ResponseEntity.status(status.getHttpStatus()).body(new ExceptionResponse(List.of(status.getMessage())));
    }

    @ExceptionHandler({ProfileException.class})
    public ResponseEntity<Object> handleProfileException(final ProfileException ex) {
        final ProfileStatus status = ex.getStatus();

        log.warn("Profile exception occurrence: {}", status.getMessage());

        return ResponseEntity.status(status.getHttpStatus()).body(new ExceptionResponse(List.of(status.getMessage())));
    }
}
