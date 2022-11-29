package me.tiary.controller.profilecontroller;

import me.tiary.controller.ProfileController;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("[ProfileController] checkNicknameDuplication")
class CheckNicknameDuplicationTest {
    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(profileController)
                .build();
    }

    @Test
    @DisplayName("[Success] nickname does not exist")
    void successIfNicknameDoesNotExist() throws Exception {
        // Given
        final String url = "/api/profile/nickname/Test";

        doReturn(false)
                .when(profileService)
                .checkNicknameDuplication(any(String.class));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Success] nickname does exist")
    void successIfNicknameDoesExist() throws Exception {
        // Given
        final String url = "/api/profile/nickname/Test";

        doReturn(true)
                .when(profileService)
                .checkNicknameDuplication(eq("Test"));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isOk());
    }
}