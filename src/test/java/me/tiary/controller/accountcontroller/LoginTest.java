package me.tiary.controller.accountcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.AccountApiUrl;
import common.factory.dto.account.AccountLoginRequestDtoFactory;
import common.factory.dto.account.AccountLoginResponseDtoFactory;
import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.controller.AccountController;
import me.tiary.dto.account.AccountLoginRequestDto;
import me.tiary.dto.account.AccountLoginResponseDto;
import me.tiary.exception.AccountException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.AccountStatus;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.service.AccountService;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[AccountController] login")
class LoginTest {
    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        gson = new Gson();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "test"})
    @DisplayName("[Fail] email is invalid")
    void failIfEmailIsInvalid(final String email) throws Exception {
        // Given
        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.create(
                email, FactoryPreset.PASSWORD
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.LOGIN.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[Fail] password is invalid")
    void failIfPasswordIsInvalid(final String password) throws Exception {
        // Given
        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.create(FactoryPreset.EMAIL, password);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.LOGIN.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email does not exist")
    void failIfEmailDoesNotExist() throws Exception {
        // Given
        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.createDefaultAccountLoginRequestDto();

        doThrow(new AccountException(AccountStatus.NOT_EXISTING_EMAIL))
                .when(accountService)
                .login(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.LOGIN.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(AccountStatus.NOT_EXISTING_EMAIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.NOT_EXISTING_EMAIL.getMessage());
    }

    @Test
    @DisplayName("[Fail] password does not match")
    void failIfPasswordDoesNotMatch() throws Exception {
        // Given
        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.createDefaultAccountLoginRequestDto();

        doThrow(new AccountException(AccountStatus.NOT_MATCHING_PASSWORD))
                .when(accountService)
                .login(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.LOGIN.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(AccountStatus.NOT_MATCHING_PASSWORD.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.NOT_MATCHING_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("[Success] account is acceptable")
    void successIfAccountIsAcceptable() throws Exception {
        // Given
        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.createDefaultAccountLoginRequestDto();

        final AccountLoginResponseDto responseDto = AccountLoginResponseDtoFactory.createDefaultAccountLoginResponseDto();

        final JwtProvider accessTokenProvider = JwtProviderFactory.createAccessTokenProvider();

        final JwtProvider refreshTokenProvider = JwtProviderFactory.createRefreshTokenProvider();

        doReturn(responseDto)
                .when(accountService)
                .login(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.LOGIN.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        final Cookie accessTokenCookie = resultActions.andReturn()
                .getResponse()
                .getCookie(AccessTokenProperties.COOKIE_NAME);

        final Cookie refreshTokenCookie = resultActions.andReturn()
                .getResponse()
                .getCookie(RefreshTokenProperties.COOKIE_NAME);

        // Then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(cookie().exists(AccessTokenProperties.COOKIE_NAME));
        resultActions.andExpect(cookie().exists(RefreshTokenProperties.COOKIE_NAME));
        resultActions.andExpect(cookie().httpOnly(AccessTokenProperties.COOKIE_NAME, true));
        resultActions.andExpect(cookie().httpOnly(RefreshTokenProperties.COOKIE_NAME, true));
        resultActions.andExpect(cookie().path(AccessTokenProperties.COOKIE_NAME, "/"));
        resultActions.andExpect(cookie().path(RefreshTokenProperties.COOKIE_NAME, "/"));
        resultActions.andExpect(cookie().secure(AccessTokenProperties.COOKIE_NAME, true));
        resultActions.andExpect(cookie().secure(RefreshTokenProperties.COOKIE_NAME, true));

        assertDoesNotThrow(() -> accessTokenProvider.verify(accessTokenCookie.getValue()));
        assertDoesNotThrow(() -> refreshTokenProvider.verify(refreshTokenCookie.getValue()));
    }
}