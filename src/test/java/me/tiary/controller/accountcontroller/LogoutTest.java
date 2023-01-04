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
import org.mockito.InjectMocks;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[AccountController] logout")
class LogoutTest {
    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("[Success] account logout is acceptable")
    void successIfAccountLogoutIsAcceptable() throws Exception {
        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.LOGOUT.getEntireUrl())
        );

        // Then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(cookie().doesNotExist(AccessTokenProperties.COOKIE_NAME));
        resultActions.andExpect(cookie().doesNotExist(RefreshTokenProperties.COOKIE_NAME));
    }
}
