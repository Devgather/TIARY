package me.tiary.controller.tilcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.TilApiUrl;
import common.factory.dto.til.TilEditRequestDtoFactory;
import common.factory.dto.til.TilEditResponseDtoFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.TilController;
import me.tiary.dto.til.TilEditRequestDto;
import me.tiary.dto.til.TilEditResponseDto;
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
@DisplayName("[TilController] updateTil")
class UpdateTilTest {
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
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_EDIT.getEntireUrl() + tilUuid;

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.create(title, FactoryPreset.CONTENT, FactoryPreset.TAGS);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
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
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_EDIT.getEntireUrl() + tilUuid;

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.create(FactoryPreset.TITLE, content, FactoryPreset.TAGS);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_EDIT.getEntireUrl() + tilUuid;

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.createDefaultTilEditRequestDto();

        doThrow(new TilException(TilStatus.NOT_EXISTING_TIL))
                .when(tilService)
                .updateTil(memberDetails.getProfileUuid(), tilUuid, requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
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
        resultActions.andExpect(status().is(TilStatus.NOT_EXISTING_TIL.getHttpStatus().value()));

        assertThat(response.getMessages()).contains(TilStatus.NOT_EXISTING_TIL.getMessage());
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_EDIT.getEntireUrl() + tilUuid;

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.createDefaultTilEditRequestDto();

        doThrow(new TilException(TilStatus.NOT_AUTHORIZED_MEMBER))
                .when(tilService)
                .updateTil(memberDetails.getProfileUuid(), tilUuid, requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
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
        resultActions.andExpect(status().is(TilStatus.NOT_AUTHORIZED_MEMBER.getHttpStatus().value()));

        assertThat(response.getMessages()).contains(TilStatus.NOT_AUTHORIZED_MEMBER.getMessage());
    }

    @Test
    @DisplayName("[Success] til is acceptable")
    void successIfTilIsAcceptable() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_EDIT.getEntireUrl() + tilUuid;

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.createDefaultTilEditRequestDto();

        final TilEditResponseDto responseDto = TilEditResponseDtoFactory.createDefaultTilEditResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .updateTil(memberDetails.getProfileUuid(), tilUuid, requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        final TilEditResponseDto result = gson.fromJson(resultActions
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilEditResponseDto.class);

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(result.getTilUuid()).isEqualTo(responseDto.getTilUuid());
    }
}