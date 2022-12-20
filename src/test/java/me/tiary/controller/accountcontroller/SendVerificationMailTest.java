package me.tiary.controller.accountcontroller;

import annotation.controller.ControllerTest;
import com.google.gson.Gson;
import config.factory.FactoryPreset;
import config.url.AccountApiUrl;
import me.tiary.controller.AccountController;
import me.tiary.exception.AccountException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.AccountStatus;
import me.tiary.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[AccountController] sendVerificationMail")
class SendVerificationMailTest {
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
    @DisplayName("[Fail] mail sender is not working")
    void failIfMailSenderIsNotWorking() throws Exception {
        // Given
        final String url = AccountApiUrl.VERIFICATION_MAIL_DELIVERY.getEntireUrl() + FactoryPreset.EMAIL;

        doThrow(new MessagingException(""))
                .when(accountService)
                .sendVerificationMail(any(String.class));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
        );

        // Then
        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("[Fail] email does exist")
    void failIfEmailDoesExist() throws Exception {
        // Given
        final String url = AccountApiUrl.VERIFICATION_MAIL_DELIVERY.getEntireUrl() + FactoryPreset.EMAIL;

        doThrow(new AccountException(AccountStatus.EXISTING_EMAIL))
                .when(accountService)
                .sendVerificationMail(eq(FactoryPreset.EMAIL));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
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
    @DisplayName("[Success] verification mail is sent")
    void successIfVerificationMailIsSent() throws Exception {
        // Given
        final String url = AccountApiUrl.VERIFICATION_MAIL_DELIVERY.getEntireUrl() + FactoryPreset.EMAIL;

        doNothing()
                .when(accountService)
                .sendVerificationMail(eq(FactoryPreset.EMAIL));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
        );

        // Then
        resultActions.andExpect(status().isCreated());
    }
}