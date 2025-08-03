package me.tiary.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tiary.common.dto.ExceptionResponse;
import me.tiary.common.util.http.HttpRequestUtils;
import org.slf4j.event.Level;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    public static final String LOG_FORMAT = "exception={}; path={}; messages={}";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception exception,
                                                  final HttpServletRequest request,
                                                  final Locale locale) {
        final List<String> messages = List.of(messageSource.getMessage("unhandled.exception", null, locale));

        logException(Level.ERROR, exception, HttpRequestUtils.getRequestUriWithQueryString(request), messages);

        return ResponseEntity.internalServerError().body(new ExceptionResponse(messages, LocalDateTime.now()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception,
                                                                                final HttpServletRequest request,
                                                                                final Locale locale) {
        final List<String> messages = List.of(messageSource.getMessage("required", new Object[]{exception.getParameterName()}, locale));

        logException(Level.WARN, exception, HttpRequestUtils.getRequestUriWithQueryString(request), messages);

        return ResponseEntity.badRequest().body(new ExceptionResponse(messages, LocalDateTime.now()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException exception,
                                                                     final HttpServletRequest request) {
        final List<String> messages = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        logException(Level.WARN, exception, HttpRequestUtils.getRequestUriWithQueryString(request), messages);

        return ResponseEntity.badRequest().body(new ExceptionResponse(messages, LocalDateTime.now()));
    }

    private void logException(final Level level, final Exception exception, final String path, final List<String> messages) {
        final String exceptionName = exception.getClass().getName();

        if (level == Level.ERROR) {
            log.error(LOG_FORMAT, exceptionName, path, messages, exception);
        } else {
            log.atLevel(level).log(LOG_FORMAT, exceptionName, path, messages);
        }
    }

}
