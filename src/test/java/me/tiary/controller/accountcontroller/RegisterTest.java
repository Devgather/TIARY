package me.tiary.controller.accountcontroller;

import com.google.gson.Gson;
import factory.dto.account.AccountCreationRequestDtoFactory;
import factory.dto.account.AccountCreationResponseDtoFactory;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("[AccountController] register")
class RegisterTest {
    public static final String URL = "/api/account";

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
    @DisplayName("[Fail] profile uuid is null")
    void failIfProfileUuidIsNull() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                null, "test@example.com", "test"
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] profile uuid is empty")
    void failIfProfileUuidIsEmpty() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                "", "test@example.com", "test"
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] profile uuid is blank")
    void failIfProfileUuidIsBlank() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                " ", "test@example.com", "test"
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email is null")
    void failIfEmailIsNull() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), null, "test"
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), "", "test"
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), " ", "test"
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), "test", "test"
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] password is null")
    void failIfPasswordIsNull() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), "test@example.com", null
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] password is empty")
    void failIfPasswordIsEmpty() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), "test@example.com", ""
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] password is blank")
    void failIfPasswordIsBlank() throws Exception {
        // Given
        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.create(
                UUID.randomUUID().toString(), "test@example.com", " "
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
                UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.EXISTING_EMAIL))
                .when(accountService)
                .register(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
                UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION))
                .when(accountService)
                .register(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
                UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.UNVERIFIED_EMAIL))
                .when(accountService)
                .register(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
                UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.NOT_EXISTING_PROFILE_UUID))
                .when(accountService)
                .register(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
                UUID.randomUUID().toString()
        );

        doThrow(new AccountException(AccountStatus.EXISTING_ANOTHER_ACCOUNT_ON_PROFILE))
                .when(accountService)
                .register(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
                UUID.randomUUID().toString()
        );

        final AccountCreationResponseDto responseDto = AccountCreationResponseDtoFactory.createDefaultAccountCreationResponseDto();

        doReturn(responseDto)
                .when(accountService)
                .register(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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