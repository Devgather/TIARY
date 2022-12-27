package me.tiary.controller.accountcontroller;

import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.AccountApiUrl;
import me.tiary.controller.AccountController;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[AccountController] checkEmailExistence")
class CheckEmailExistenceTest {
    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    @DisplayName("[Success] email does not exist")
    void successIfEmailDoesNotExist() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_EXISTENCE_CHECK.getEntireUrl() + FactoryPreset.EMAIL;

        doReturn(false)
                .when(accountService)
                .checkEmailExistence(any(String.class));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Success] email does exist")
    void successIfEmailDoesExist() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_EXISTENCE_CHECK.getEntireUrl() + FactoryPreset.EMAIL;

        doReturn(true)
                .when(accountService)
                .checkEmailExistence(eq(FactoryPreset.EMAIL));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isOk());
    }
}