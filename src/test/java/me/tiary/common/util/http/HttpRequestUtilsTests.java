package me.tiary.common.util.http;

import jakarta.servlet.http.HttpServletRequest;
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

        @Test
        void shouldReturnRequestUriWithoutQueryString_whenQueryStringIsNull() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);

            given(request.getRequestURI())
                    .willReturn("/test");

            given(request.getQueryString())
                    .willReturn(null);

            // When
            String result = HttpRequestUtils.getRequestUriWithQueryString(request);

            // Then
            assertThat(result).isEqualTo("/test");
        }

        @Test
        void shouldReturnRequestUriWithQueryString_whenQueryStringIsNotNull() {
            // Given
            HttpServletRequest request = mock(HttpServletRequest.class);

            given(request.getRequestURI())
                    .willReturn("/test");

            given(request.getQueryString())
                    .willReturn("data=test");

            // When
            String result = HttpRequestUtils.getRequestUriWithQueryString(request);

            // Then
            assertThat(result).isEqualTo("/test?data=test");
        }

    }

}
