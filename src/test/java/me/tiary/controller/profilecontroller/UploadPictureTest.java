package me.tiary.controller.profilecontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.ProfileApiUrl;
import common.factory.dto.profile.ProfilePictureUploadRequestDtoFactory;
import common.factory.dto.profile.ProfilePictureUploadResponseDtoFactory;
import common.factory.mock.web.MockMultipartFileFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.ProfileController;
import me.tiary.dto.profile.ProfilePictureUploadRequestDto;
import me.tiary.dto.profile.ProfilePictureUploadResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[ProfileController] uploadPicture")
class UploadPictureTest {
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
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() throws Exception {
        // Given
        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.createDefaultProfilePictureUploadRequestDto();

        doThrow(new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE))
                .when(profileService)
                .uploadPicture(eq(memberDetails.getProfileUuid()), any(ProfilePictureUploadRequestDto.class));

        // When
        final MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(ProfileApiUrl.PROFILE_PICTURE_UPLOAD.getEntireUrl());

        builder.with(request -> {
            request.setMethod("PATCH");

            return request;
        });

        final ResultActions resultActions = mockMvc.perform(builder
                .file((MockMultipartFile) requestDto.getPictureFile()));

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
    @DisplayName("[Fail] content type is null")
    void failIfContentTypeIsNull() throws Exception {
        // Given
        final MultipartFile multipartFile = MockMultipartFileFactory.create("image", "image.png", null, 1);

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.create(multipartFile);

        doThrow(new ProfileException(ProfileStatus.NOT_EXISTING_CONTENT_TYPE))
                .when(profileService)
                .uploadPicture(eq(memberDetails.getProfileUuid()), any(ProfilePictureUploadRequestDto.class));

        // When
        final MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(ProfileApiUrl.PROFILE_PICTURE_UPLOAD.getEntireUrl());

        builder.with(request -> {
            request.setMethod("PATCH");

            return request;
        });

        final ResultActions resultActions = mockMvc.perform(builder
                .file((MockMultipartFile) requestDto.getPictureFile()));

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(ProfileStatus.NOT_EXISTING_CONTENT_TYPE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(ProfileStatus.NOT_EXISTING_CONTENT_TYPE.getMessage());
    }

    @Test
    @DisplayName("[Fail] content type is improper")
    void failIfContentTypeIsImproper() throws Exception {
        // Given
        final MultipartFile multipartFile = MockMultipartFileFactory.createDefaultTxtMockMultipartFile(1);

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.create(multipartFile);

        doThrow(new ProfileException(ProfileStatus.NOT_SUPPORTING_CONTENT_TYPE))
                .when(profileService)
                .uploadPicture(eq(memberDetails.getProfileUuid()), any(ProfilePictureUploadRequestDto.class));

        // When
        final MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(ProfileApiUrl.PROFILE_PICTURE_UPLOAD.getEntireUrl());

        builder.with(request -> {
            request.setMethod("PATCH");

            return request;
        });

        final ResultActions resultActions = mockMvc.perform(builder
                .file((MockMultipartFile) requestDto.getPictureFile()));

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(ProfileStatus.NOT_SUPPORTING_CONTENT_TYPE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(ProfileStatus.NOT_SUPPORTING_CONTENT_TYPE.getMessage());
    }

    @Test
    @DisplayName("[Success] picture is acceptable")
    void successIfPictureIsAcceptable() throws Exception {
        // Given
        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.createDefaultProfilePictureUploadRequestDto();

        final ProfilePictureUploadResponseDto responseDto = ProfilePictureUploadResponseDtoFactory.createDefaultProfilePictureUploadResponseDto();

        doReturn(responseDto)
                .when(profileService)
                .uploadPicture(eq(memberDetails.getProfileUuid()), any(ProfilePictureUploadRequestDto.class));

        // When
        final MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(ProfileApiUrl.PROFILE_PICTURE_UPLOAD.getEntireUrl());

        builder.with(request -> {
            request.setMethod("PATCH");

            return request;
        });

        final ResultActions resultActions = mockMvc.perform(builder
                .file((MockMultipartFile) requestDto.getPictureFile()));

        final ProfilePictureUploadResponseDto response = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), ProfilePictureUploadResponseDto.class);

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getPicture()).isEqualTo(responseDto.getPicture());
    }
}
