package me.tiary.utility.common.cookieutility;

import me.tiary.utility.common.CookieUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[CookieUtility] createCookie")
class CreateCookieTest {
    @Test
    @DisplayName("[Success] cookie is created")
    void successIfCookieIsCreated() {
        // When
        final Cookie result = CookieUtility.createCookie("name", "value");

        // Then
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getValue()).isEqualTo("value");
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
    }

    @Test
    @DisplayName("[Success] cookie is created with max age")
    void successIfCookieIsCreatedWithMaxAge() {
        // When
        final Cookie result = CookieUtility.createCookie("name", "value", 60);

        // Then
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getValue()).isEqualTo("value");
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isEqualTo(60);
    }
}