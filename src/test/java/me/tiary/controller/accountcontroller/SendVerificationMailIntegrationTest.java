package me.tiary.controller.accountcontroller;

import annotation.controller.ControllerIntegrationTest;
import config.url.AccountApiUrl;
import me.tiary.controller.AccountController;
import me.tiary.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(AccountController.class)
@DisplayName("[AccountController - Integration] sendVerificationMail")
class SendVerificationMailIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("[Fail] email is blank")
    void failIfEmailIsBlank() throws Exception {
        // Given
        final String url = AccountApiUrl.VERIFICATION_MAIL_DELIVERY.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] email does not satisfy format")
    void failIfEmailDoesNotSatisfyFormat() throws Exception {
        // Given
        final String url = AccountApiUrl.VERIFICATION_MAIL_DELIVERY.getEntireUrl() + "test";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}