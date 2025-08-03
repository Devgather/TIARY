package me.tiary.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import me.tiary.common.dto.ExceptionResponse;
import me.tiary.common.util.http.HttpRequestUtils;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTests {

    static final String PATH = "/test?data=test";

    static final String MESSAGE = "test";

    static MockedStatic<HttpRequestUtils> httpRequestUtils;

    static LogCaptor logCaptor;

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @Mock
    MessageSource messageSource;

    @BeforeAll
    static void initAll() {
        httpRequestUtils = mockStatic(HttpRequestUtils.class);

        given(HttpRequestUtils.getRequestUriWithQueryString(any(HttpServletRequest.class)))
                .willReturn(PATH);

        logCaptor = LogCaptor.forClass(GlobalExceptionHandler.class);
    }

    @AfterEach
    void tearDown() {
        logCaptor.clearLogs();
    }

    @AfterAll
    static void tearDownAll() {
        httpRequestUtils.close();
        logCaptor.close();
    }

    @Nested
    class HandleExceptionTest {

        Exception exception;

        HttpServletRequest request;

        Locale locale;

        @BeforeEach
        void init() {
            exception = new Exception();

            request = new MockHttpServletRequest();

            locale = Locale.getDefault();

            given(messageSource.getMessage(any(String.class), isNull(), eq(locale)))
                    .willReturn(MESSAGE);
        }

        @Test
        void shouldLogException_whenHandleExceptionIsCalled() {
            // When
            globalExceptionHandler.handleException(exception, request, locale);

            // Then
            assertThat(logCaptor.hasErrorMessage(GlobalExceptionHandlerHelper.generateExceptionLog(exception, PATH, List.of(MESSAGE)))).isTrue();
        }

        @Test
        void shouldReturnInternalServerError_whenHandleExceptionIsCalled() {
            // When
            ResponseEntity<Object> result = globalExceptionHandler.handleException(exception, request, locale);
            ExceptionResponse response = (ExceptionResponse) result.getBody();

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(MESSAGE);
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

    }

    @Nested
    class HandleMissingServletRequestParameterExceptionTest {

        MissingServletRequestParameterException exception;

        HttpServletRequest request;

        Locale locale;

        @BeforeEach
        void init() {
            exception = new MissingServletRequestParameterException("data", "String");

            request = new MockHttpServletRequest();

            locale = Locale.getDefault();

            given(messageSource.getMessage(any(String.class), any(Object[].class), eq(locale)))
                    .willReturn(MESSAGE);
        }

        @Test
        void shouldLogException_whenHandleMissingServletRequestParameterExceptionIsCalled() {
            // When
            globalExceptionHandler.handleMissingServletRequestParameterException(exception, request, locale);

            // Then
            assertThat(logCaptor.hasWarnMessage(GlobalExceptionHandlerHelper.generateExceptionLog(exception, PATH, List.of(MESSAGE)))).isTrue();
        }

        @Test
        void shouldReturnBadRequest_whenHandleMissingServletRequestParameterExceptionIsCalled() {
            // When
            ResponseEntity<Object> result = globalExceptionHandler.handleMissingServletRequestParameterException(exception, request, locale);
            ExceptionResponse response = (ExceptionResponse) result.getBody();

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(MESSAGE);
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

    }

    @Nested
    class HandleConstraintViolationExceptionTest {

        ConstraintViolationException exception;

        HttpServletRequest request;

        @BeforeEach
        void init() {
            ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);

            given(constraintViolation.getMessage())
                    .willReturn(MESSAGE);

            exception = new ConstraintViolationException(Set.of(constraintViolation));

            request = new MockHttpServletRequest();
        }

        @Test
        void shouldLogException_whenHandleConstraintViolationExceptionIsCalled() {
            // When
            globalExceptionHandler.handleConstraintViolationException(exception, request);

            // Then
            assertThat(logCaptor.hasWarnMessage(GlobalExceptionHandlerHelper.generateExceptionLog(exception, PATH, List.of(MESSAGE)))).isTrue();
        }

        @Test
        void shouldReturnBadRequest_whenHandleConstraintViolationExceptionIsCalled() {
            // When
            ResponseEntity<Object> result = globalExceptionHandler.handleConstraintViolationException(exception, request);
            ExceptionResponse response = (ExceptionResponse) result.getBody();

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(MESSAGE);
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

    }

}
