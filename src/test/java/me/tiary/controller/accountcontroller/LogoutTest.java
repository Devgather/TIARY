package me.tiary.controller.accountcontroller;

import common.annotation.controller.ControllerTest;
import common.config.url.AccountApiUrl;
import me.tiary.controller.AccountController;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[AccountController] logout")
class LogoutTest {
    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        final AccountController accountController = new AccountController(null);

        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("[Success] account logout is acceptable")
    void successIfAccountLogoutIsAcceptable() throws Exception {
        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(AccountApiUrl.LOGOUT.getEntireUrl())
        );

        // Then
        resultActions.andExpect(status().isNoContent());
        resultActions.andExpect(cookie().exists(AccessTokenProperties.COOKIE_NAME));
        resultActions.andExpect(cookie().exists(RefreshTokenProperties.COOKIE_NAME));
        resultActions.andExpect(cookie().httpOnly(AccessTokenProperties.COOKIE_NAME, true));
        resultActions.andExpect(cookie().httpOnly(RefreshTokenProperties.COOKIE_NAME, true));
        resultActions.andExpect(cookie().path(AccessTokenProperties.COOKIE_NAME, "/"));
        resultActions.andExpect(cookie().path(RefreshTokenProperties.COOKIE_NAME, "/"));
        resultActions.andExpect(cookie().secure(AccessTokenProperties.COOKIE_NAME, true));
        resultActions.andExpect(cookie().secure(RefreshTokenProperties.COOKIE_NAME, true));
        resultActions.andExpect(cookie().value(AccessTokenProperties.COOKIE_NAME, (String) null));
        resultActions.andExpect(cookie().value(RefreshTokenProperties.COOKIE_NAME, (String) null));
    }
}