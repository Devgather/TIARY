package me.tiary.common.util.web;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class HttpRequestUtilsTests {

    @Nested
    class GetRequestUriWithQueryStringTest {

        static final String REQUEST_URI = "/test";

        static final String QUERY_STRING = "data=test";

        HttpServletRequest request;

        @BeforeEach
        void init() {
            request = mock(HttpServletRequest.class);

            given(request.getRequestURI())
                    .willReturn(REQUEST_URI);
        }

        @Test
        void shouldReturnRequestUriWithoutQueryString_whenQueryStringIsNull() {
            // Given
            given(request.getQueryString())
                    .willReturn(null);

            // When
            String result = HttpRequestUtils.getRequestUriWithQueryString(request);

            // Then
            assertThat(result).isEqualTo(REQUEST_URI);
        }

        @Test
        void shouldReturnRequestUriWithQueryString_whenQueryStringIsNotNull() {
            // Given
            given(request.getQueryString())
                    .willReturn(QUERY_STRING);

            // When
            String result = HttpRequestUtils.getRequestUriWithQueryString(request);

            // Then
            assertThat(result).isEqualTo(REQUEST_URI + '?' + QUERY_STRING);
        }

    }

}
