package me.tiary.controller.accountcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.AccountApiUrl;
import common.factory.dto.account.AccountVerificationRequestDtoFactory;
import common.factory.dto.account.AccountVerificationResponseDtoFactory;
import me.tiary.controller.AccountController;
import me.tiary.domain.Verification;
import me.tiary.dto.account.AccountVerificationRequestDto;
import me.tiary.dto.account.AccountVerificationResponseDto;
import me.tiary.exception.AccountException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.AccountStatus;
import me.tiary.service.AccountService;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[AccountController] verifyEmail")
class VerifyEmailTest {
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

    @Test
    @DisplayName("[Fail] email is null")
    void failIfEmailIsNull() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.create(
                null, StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email is empty")
    void failIfEmailIsEmpty() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.create(
                "", StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email is blank")
    void failIfEmailIsBlank() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.create(
                " ", StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email is invalid format")
    void failIfEmailIsInvalidFormat() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.create(
                "test", StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] code is null")
    void failIfCodeIsNull() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                null
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] code is empty")
    void failIfCodeIsEmpty() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                ""
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] code is blank")
    void failIfCodeIsBlank() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                " "
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] code does not meet min length")
    void failIfCodeDoesNotMeetMinLength() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH - 1)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] code exceeds max length")
    void failIfCodeExceedsMaxLength() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH + 1)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email verification is not requested")
    void failEmailVerificationIsNotRequested() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        doThrow(new AccountException(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION))
                .when(accountService)
                .verifyEmail(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
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
        resultActions.andExpect(status().is(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION.getMessage());
    }

    @Test
    @DisplayName("[Fail] email is not unverified")
    void failIfEmailIsNotUnverified() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        doThrow(new AccountException(AccountStatus.VERIFIED_EMAIL))
                .when(accountService)
                .verifyEmail(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
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
        resultActions.andExpect(status().is(AccountStatus.VERIFIED_EMAIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.VERIFIED_EMAIL.getMessage());
    }

    @Test
    @DisplayName("[Fail] code is incorrect")
    void failIfCodeIsIncorrect() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        doThrow(new AccountException(AccountStatus.INCORRECT_CODE))
                .when(accountService)
                .verifyEmail(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
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
        resultActions.andExpect(status().is(AccountStatus.INCORRECT_CODE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.INCORRECT_CODE.getMessage());
    }

    @Test
    @DisplayName("[Success] email is verified")
    void successIfEmailIsVerified() throws Exception {
        // Given
        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        final AccountVerificationResponseDto responseDto = AccountVerificationResponseDtoFactory.createDefaultAccountVerificationResponseDto();

        doReturn(responseDto)
                .when(accountService)
                .verifyEmail(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        final AccountVerificationResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                AccountVerificationResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getUuid()).isEqualTo(responseDto.getUuid());
    }
}