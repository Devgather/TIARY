package me.tiary.utility.common.cookieutility;

import me.tiary.utility.common.CookieUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[CookieUtility] deleteCookie")
class DeleteCookieTest {
    private MockHttpServletResponse response;

    @BeforeEach
    void beforeEach() {
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("[Success] cookie is deleted")
    void successIfCookieIsDeleted() {
        // When
        CookieUtility.deleteCookie(response, "name");

        final Cookie result = response.getCookie("name");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getValue()).isNull();
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isZero();
    }
}