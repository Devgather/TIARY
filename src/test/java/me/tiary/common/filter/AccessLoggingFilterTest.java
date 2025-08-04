package me.tiary.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.tiary.common.util.web.HttpRequestUtils;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessLoggingFilterTest {

    static final String REMOTE_ADDRESS = "127.0.0.1";

    static final String METHOD = "GET";

    static final String REQUEST_URI = "/test?data=test";

    static final String PROTOCOL = "HTTP/1.1";

    static final int STATUS = 200;

    static MockedStatic<HttpRequestUtils> httpRequestUtils;

    static LogCaptor logCaptor;

    AccessLoggingFilter filter;

    HttpServletRequest request;

    HttpServletResponse response;

    FilterChain filterChain;

    @BeforeAll
    static void initAll() {
        httpRequestUtils = mockStatic(HttpRequestUtils.class);

        given(HttpRequestUtils.getRequestUriWithQueryString(any(HttpServletRequest.class)))
                .willReturn(REQUEST_URI);

        logCaptor = LogCaptor.forClass(AccessLoggingFilter.class);
    }

    @BeforeEach
    void init() {
        filter = new AccessLoggingFilter();

        request = mock(HttpServletRequest.class);

        given(request.getRemoteAddr())
                .willReturn(REMOTE_ADDRESS);

        given(request.getMethod())
                .willReturn(METHOD);

        given(request.getProtocol())
                .willReturn(PROTOCOL);

        response = mock(HttpServletResponse.class);

        given(response.getStatus())
                .willReturn(STATUS);

        filterChain = mock(FilterChain.class);
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

    @Test
    void shouldInvokeFilterChain_whenDoFilterInternalIsCalled() throws Exception {
        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldLogAccessInformation_whenDoFilterInternalIsCalled() throws Exception {
        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(logCaptor.hasInfoMessage(AccessLoggingFilterHelper.generateAccessLog(REMOTE_ADDRESS, METHOD, REQUEST_URI, PROTOCOL, STATUS))).isTrue();
    }

}
