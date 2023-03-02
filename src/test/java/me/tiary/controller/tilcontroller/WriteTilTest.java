package me.tiary.controller.tilcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.TilApiUrl;
import common.factory.dto.til.TilWritingRequestDtoFactory;
import common.factory.dto.til.TilWritingResponseDtoFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.TilController;
import me.tiary.dto.til.TilWritingRequestDto;
import me.tiary.dto.til.TilWritingResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.TilStatus;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TilService;
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
@DisplayName("[TilController] writeTil")
class WriteTilTest {
    @InjectMocks
    private TilController tilController;

    @Mock
    private TilService tilService;

    private MemberDetails memberDetails;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        memberDetails = MemberDetails.builder()
                .profileUuid(UUID.randomUUID().toString())
                .build();

        mockMvc = MockMvcBuilders
                .standaloneSetup(tilController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver(memberDetails))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        gson = new Gson();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("[Fail] title is invalid")
    void failIfTitleIsInvalid(final String title) throws Exception {
        // Given
        final String url = TilApiUrl.TIL_WRITING.getEntireUrl();

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.create(title, FactoryPreset.CONTENT, FactoryPreset.TAGS);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("[Fail] content is invalid")
    void failIfContentIsInvalid(final String content) throws Exception {
        // Given
        final String url = TilApiUrl.TIL_WRITING.getEntireUrl();

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.create(FactoryPreset.TITLE, content, FactoryPreset.TAGS);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
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
        final String url = TilApiUrl.TIL_WRITING.getEntireUrl();

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.createDefaultWritingRequestDto();

        doThrow(new TilException(TilStatus.NOT_EXISTING_PROFILE))
                .when(tilService)
                .writeTil(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(TilStatus.NOT_EXISTING_PROFILE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TilStatus.NOT_EXISTING_PROFILE.getMessage());
    }

    @Test
    @DisplayName("[Success] til is acceptable with tags is null")
    void successIfTilIsAcceptableWithTagsIsNull() throws Exception {
        // Given
        final String url = TilApiUrl.TIL_WRITING.getEntireUrl();

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.create(FactoryPreset.TITLE, FactoryPreset.CONTENT, null);

        final TilWritingResponseDto responseDto = TilWritingResponseDtoFactory.createDefaultTilWritingResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .writeTil(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final TilWritingResponseDto response = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), TilWritingResponseDto.class);

        // Then
        resultActions.andExpect(status().isCreated());
        assertThat(response.getUuid()).hasSize(36);
    }

    @Test
    @DisplayName("[Success] til is acceptable with tags is not null")
    void successIfTilIsAcceptableWithTagsIsNotNull() throws Exception {
        // Given
        final String url = TilApiUrl.TIL_WRITING.getEntireUrl();

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.createDefaultWritingRequestDto();

        final TilWritingResponseDto responseDto = TilWritingResponseDtoFactory.createDefaultTilWritingResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .writeTil(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final TilWritingResponseDto response = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), TilWritingResponseDto.class);

        // Then
        resultActions.andExpect(status().isCreated());
        assertThat(response.getUuid()).hasSize(36);
    }
}