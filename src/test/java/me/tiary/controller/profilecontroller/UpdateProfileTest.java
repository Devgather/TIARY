package me.tiary.controller.profilecontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.ProfileApiUrl;
import common.factory.dto.profile.ProfileUpdateRequestDtoFactory;
import common.factory.dto.profile.ProfileUpdateResponseDtoFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.ProfileController;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileUpdateRequestDto;
import me.tiary.dto.profile.ProfileUpdateResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.security.web.userdetails.MemberDetails;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[ProfileController] updateProfile")
class UpdateProfileTest {
    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    private MemberDetails memberDetails;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        memberDetails = MemberDetails.builder()
                .profileUuid(UUID.randomUUID().toString())
                .build();

        mockMvc = MockMvcBuilders
                .standaloneSetup(profileController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver(memberDetails))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        gson = new Gson();
    }

    @Test
    @DisplayName("[Fail] nickname is null")
    void failIfNicknameIsNull() throws Exception {
        // Given
        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.create(null);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ProfileApiUrl.PROFILE_UPDATE.getEntireUrl())
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
        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.create("");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ProfileApiUrl.PROFILE_UPDATE.getEntireUrl())
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
        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.create(" ");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ProfileApiUrl.PROFILE_UPDATE.getEntireUrl())
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

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.create(nickname);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ProfileApiUrl.PROFILE_UPDATE.getEntireUrl())
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_UPDATE.getEntireUrl();

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.createDefaultProfileUpdateRequestDto();

        doThrow(new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE))
                .when(profileService)
                .updateProfile(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
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
    @DisplayName("[Fail] nickname does exist")
    void failIfNicknameDoesExist() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_UPDATE.getEntireUrl();

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.createDefaultProfileUpdateRequestDto();

        doThrow(new ProfileException(ProfileStatus.EXISTING_NICKNAME))
                .when(profileService)
                .updateProfile(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(ProfileStatus.EXISTING_NICKNAME.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(ProfileStatus.EXISTING_NICKNAME.getMessage());
    }

    @Test
    @DisplayName("[Success] profile is acceptable")
    void successIfProfileIsAcceptable() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_UPDATE.getEntireUrl();

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.createDefaultProfileUpdateRequestDto();

        final ProfileUpdateResponseDto responseDto = ProfileUpdateResponseDtoFactory.createDefaultProfileUpdateResponseDto();

        doReturn(responseDto)
                .when(profileService)
                .updateProfile(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto)));

        final ProfileUpdateResponseDto result = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), ProfileUpdateResponseDto.class);

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(result.getNickname()).isEqualTo(responseDto.getNickname());
    }
}