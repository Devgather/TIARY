package me.tiary.controller.profilecontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.ProfileApiUrl;
import common.factory.dto.profile.ProfileReadResponseDtoFactory;
import me.tiary.controller.ProfileController;
import me.tiary.dto.profile.ProfileReadResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[ProfileController] readProfile")
class ReadProfileTest {
    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(profileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        gson = new Gson();
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_READ.getEntireUrl() + FactoryPreset.NICKNAME;

        doThrow(new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE))
                .when(profileService)
                .readProfile(any(String.class));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(ProfileStatus.NOT_EXISTING_PROFILE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(ProfileStatus.NOT_EXISTING_PROFILE.getMessage());
    }

    @Test
    @DisplayName("[Success] profile does exist")
    void successIfProfileDoesExist() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_READ.getEntireUrl() + FactoryPreset.NICKNAME;

        final ProfileReadResponseDto responseDto = ProfileReadResponseDtoFactory.createDefaultProfileReadResponseDto();

        doReturn(responseDto)
                .when(profileService)
                .readProfile(FactoryPreset.NICKNAME);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        final ProfileReadResponseDto response = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), ProfileReadResponseDto.class);

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getNickname()).isEqualTo(responseDto.getNickname());
    }
}