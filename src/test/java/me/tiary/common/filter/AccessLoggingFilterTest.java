package me.tiary.common.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.tiary.common.util.http.HttpRequestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessLoggingFilterTest {

    AccessLoggingFilter filter;

    MockedStatic<HttpRequestUtils> httpRequestUtils;

    ListAppender<ILoggingEvent> loggingEventAppender;

    @BeforeEach
    void setUp() {
        filter = new AccessLoggingFilter();

        httpRequestUtils = mockStatic(HttpRequestUtils.class);

        loggingEventAppender = new ListAppender<>();
        loggingEventAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(filter.getClass());
        logger.addAppender(loggingEventAppender);
    }

    @AfterEach
    void tearDown() {
        httpRequestUtils.close();
        loggingEventAppender.stop();
    }

    @Test
    void shouldInvokeFilterChain_whenDoFilterInternalIsCalled() throws Exception {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldLogAccessInformation_whenDoFilterInternalIsCalled() throws Exception {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getRemoteAddr())
                .willReturn("127.0.0.1");

        given(request.getMethod())
                .willReturn("GET");

        given(HttpRequestUtils.getRequestUriWithQueryString(request))
                .willReturn("/test?data=test");

        given(request.getProtocol())
                .willReturn("HTTP/1.1");

        HttpServletResponse response = mock(HttpServletResponse.class);

        given(response.getStatus())
                .willReturn(200);

        FilterChain filterChain = mock(FilterChain.class);

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        ILoggingEvent loggingEvent = loggingEventAppender.list.get(0);

        assertAll(
                () -> assertThat(loggingEvent.getLevel()).isEqualTo(Level.INFO),
                () -> assertThat(loggingEvent.getFormattedMessage()).isEqualTo("127.0.0.1 - \"GET /test?data=test HTTP/1.1\" 200")
        );
    }

}
