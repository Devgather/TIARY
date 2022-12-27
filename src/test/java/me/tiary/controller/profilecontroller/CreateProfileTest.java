package me.tiary.controller.profilecontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.ProfileApiUrl;
import common.factory.dto.profile.ProfileCreationRequestDtoFactory;
import common.factory.dto.profile.ProfileCreationResponseDtoFactory;
import me.tiary.controller.ProfileController;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.dto.profile.ProfileCreationResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.service.ProfileService;
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
@DisplayName("[ProfileController] createProfile")
class CreateProfileTest {
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
    @DisplayName("[Fail] nickname is null")
    void failIfNicknameIsNull() throws Exception {
        // Given
        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.create(null);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(ProfileApiUrl.PROFILE_CREATION.getEntireUrl())
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] nickname is empty")
    void failIfNicknameIsEmpty() throws Exception {
        // Given
        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.create("");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(ProfileApiUrl.PROFILE_CREATION.getEntireUrl())
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] nickname is blank")
    void failIfNicknameIsBlank() throws Exception {
        // Given
        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.create(" ");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(ProfileApiUrl.PROFILE_CREATION.getEntireUrl())
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] nickname exceeds max length")
    void failIfNicknameExceedsMaxLength() throws Exception {
        // Given
        final String nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH + 1);

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.create(nickname);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(ProfileApiUrl.PROFILE_CREATION.getEntireUrl())
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] nickname does exist")
    void failIfNicknameDoesExist() throws Exception {
        // Given
        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.createDefaultProfileCreationRequestDto();

        doThrow(new ProfileException(ProfileStatus.EXISTING_NICKNAME))
                .when(profileService)
                .createProfile(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(ProfileApiUrl.PROFILE_CREATION.getEntireUrl())
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final ExceptionResponse response = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class);

        // Then
        resultActions.andExpect(status().is(ProfileStatus.EXISTING_NICKNAME.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(ProfileStatus.EXISTING_NICKNAME.getMessage());
    }

    @Test
    @DisplayName("[Success] profile is acceptable")
    void successIfProfileIsAcceptable() throws Exception {
        // Given
        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.createDefaultProfileCreationRequestDto();

        final ProfileCreationResponseDto responseDto = ProfileCreationResponseDtoFactory.createDefaultProfileCreationResponseDto();

        doReturn(responseDto).when(profileService).createProfile(eq(requestDto));

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(ProfileApiUrl.PROFILE_CREATION.getEntireUrl())
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final ProfileCreationResponseDto response = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), ProfileCreationResponseDto.class);

        // Then
        resultActions.andExpect(status().isCreated());
        assertThat(response.getUuid().length()).isEqualTo(36);
        assertThat(response.getNickname()).isEqualTo(responseDto.getNickname());
    }
}