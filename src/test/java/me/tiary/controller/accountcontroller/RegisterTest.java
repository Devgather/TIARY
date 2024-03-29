package me.tiary.controller.accountcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.AccountApiUrl;
import common.factory.dto.account.AccountCreationRequestDtoFactory;
import common.factory.dto.account.AccountCreationResponseDtoFactory;
import me.tiary.controller.AccountController;
import me.tiary.dto.account.AccountCreationRequestDto;
import me.tiary.dto.account.AccountCreationResponseDto;
import me.tiary.exception.AccountException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.AccountStatus;
import me.tiary.service.AccountService;
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

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[AccountController] register")
class RegisterTest {
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
    @ValueSource(strings = {" "})
    @DisplayName("[Fail] verification uuid is invalid")
    void failIfVerificationUuidIsInvalid(final String verificationUuid) throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                verificationUuid, UUID.randomUUID().toString(), FactoryPreset.EMAIL, FactoryPreset.PASSWORD
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("[Fail] profile uuid is invalid")
    void failIfProfileUuidIsInvalid(final String profileUuid) throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), profileUuid, FactoryPreset.EMAIL, FactoryPreset.PASSWORD
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "test"})
    @DisplayName("[Fail] email is invalid")
    void failIfEmailIsInvalid(final String email) throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), email, FactoryPreset.PASSWORD
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
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
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), FactoryPreset.EMAIL, password
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email does exist")
    void failIfEmailDoesExist() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.EXISTING_EMAIL))
                .when(accountService)
                .register(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
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
        resultActions.andExpect(status().is(AccountStatus.EXISTING_EMAIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.EXISTING_EMAIL.getMessage());
    }

    @Test
    @DisplayName("[Fail] email verification is not requested")
    void failEmailVerificationIsNotRequested() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION))
                .when(accountService)
                .register(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
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
    @DisplayName("[Fail] email is not verified")
    void failIfEmailIsNotVerified() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.UNVERIFIED_EMAIL))
                .when(accountService)
                .register(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
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
        resultActions.andExpect(status().is(AccountStatus.UNVERIFIED_EMAIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.UNVERIFIED_EMAIL.getMessage());
    }

    @Test
    @DisplayName("[Fail] profile uuid does not exist")
    void failProfileUuidDoesNotExist() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.NOT_EXISTING_PROFILE_UUID))
                .when(accountService)
                .register(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
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
        resultActions.andExpect(status().is(AccountStatus.NOT_EXISTING_PROFILE_UUID.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.NOT_EXISTING_PROFILE_UUID.getMessage());
    }

    @Test
    @DisplayName("[Fail] profile already has account")
    void failIfProfileAlreadyHasAccount() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.EXISTING_ANOTHER_ACCOUNT_ON_PROFILE))
                .when(accountService)
                .register(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
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
        resultActions.andExpect(status().is(AccountStatus.EXISTING_ANOTHER_ACCOUNT_ON_PROFILE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(AccountStatus.EXISTING_ANOTHER_ACCOUNT_ON_PROFILE.getMessage());
    }

    @Test
    @DisplayName("[Success] account is creatable")
    void successIfAccountIsCreatable() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        final AccountCreationResponseDto responseDto = AccountCreationResponseDtoFactory.createDefaultAccountCreationResponseDto();

        doReturn(responseDto)
                .when(accountService)
                .register(requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(AccountApiUrl.REGISTER.getEntireUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        final AccountCreationResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                AccountCreationResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isCreated());
        assertThat(response.getEmail()).isEqualTo(responseDto.getEmail());
    }
}