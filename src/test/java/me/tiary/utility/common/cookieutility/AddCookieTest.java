package me.tiary.utility.common.cookieutility;

import me.tiary.utility.common.CookieUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[CookieUtility] addCookie")
class AddCookieTest {
    private MockHttpServletResponse response;

    @BeforeEach
    void beforeEach() {
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("[Success] cookie is added")
    void successIfCookieIsAdded() {
        // Given
        final Cookie cookie = CookieUtility.createCookie("name", "value", 60);

        // When
        CookieUtility.addCookie(response, cookie);

        final Cookie result = response.getCookie("name");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getValue()).isEqualTo("value");
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isEqualTo(60);
    }

    @Test
    @DisplayName("[Success] cookie is created and added")
    void successIfCookieIsCreatedAndAdded() {
        // When
        CookieUtility.addCookie(response, "name", "value");

        final Cookie result = response.getCookie("name");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getValue()).isEqualTo("value");
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
    }

    @Test
    @DisplayName("[Success] cookie is created with max age and added")
    void successIfCookieIsCreatedWithMaxAgeAndAdded() {
        // When
        CookieUtility.addCookie(response, "name", "value", 60);

        final Cookie result = response.getCookie("name");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getValue()).isEqualTo("value");
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isEqualTo(60);
    }
}