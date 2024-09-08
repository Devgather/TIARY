package me.tiary.controller.tilcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.TilApiUrl;
import common.factory.dto.til.TilListReadResponseDtoFactory;
import me.tiary.controller.TilController;
import me.tiary.dto.til.TilListReadResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.TilStatus;
import me.tiary.service.TilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TilController] readTilList")
class ReadTilListTest {
    @InjectMocks
    private TilController tilController;

    @Mock
    private TilService tilService;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(tilController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        gson = new Gson();
    }

    @Test
    @DisplayName("[Fail] profile does not exist when tag is not provided")
    void failIfProfileDoesNotExistWhenTagIsNotProvided() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + nickname;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        doThrow(new TilException(TilStatus.NOT_EXISTING_PROFILE))
                .when(tilService)
                .readTilList(nickname, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("page", "0")
                        .param("size", "5")
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(TilStatus.NOT_EXISTING_PROFILE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TilStatus.NOT_EXISTING_PROFILE.getMessage());
    }

    @Test
    @DisplayName("[Success] tils do not exist when tag is not provided")
    void successIfTilsDoNotExistWhenTagIsNotProvided() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + nickname;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final TilListReadResponseDto responseDto = TilListReadResponseDtoFactory.create(new ArrayList<>(), 0);

        doReturn(responseDto)
                .when(tilService)
                .readTilList(nickname, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("page", "0")
                        .param("size", "5")
        );

        final TilListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTils()).isEmpty();
        assertThat(response.getTotalPages()).isEqualTo(responseDto.getTotalPages());
    }

    @Test
    @DisplayName("[Success] tils do exist when tag is not provided")
    void successIfTilsDoExistWhenTagIsNotProvided() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + nickname;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final TilListReadResponseDto responseDto = TilListReadResponseDtoFactory.createDefaultTilListReadResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .readTilList(nickname, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("page", "0")
                        .param("size", "5")
        );

        final TilListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTils().get(0).getUuid()).hasSize(36);
        assertThat(response.getTils().get(0).getTitle()).isEqualTo(responseDto.getTils().get(0).getTitle());
        assertThat(response.getTils().get(0).getContent()).isEqualTo(responseDto.getTils().get(0).getContent());
        assertThat(response.getTotalPages()).isEqualTo(responseDto.getTotalPages());
    }

    @Test
    @DisplayName("[Fail] profile does not exist when tag is provided")
    void failIfProfileDoesNotExistWhenTagIsProvided() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + nickname;

        final String tag = FactoryPreset.TAG;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        doThrow(new TilException(TilStatus.NOT_EXISTING_PROFILE))
                .when(tilService)
                .readTilListByTag(nickname, tag, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "0")
                        .param("size", "5")
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(TilStatus.NOT_EXISTING_PROFILE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TilStatus.NOT_EXISTING_PROFILE.getMessage());
    }

    @Test
    @DisplayName("[Fail] tag does not exist when tag is provided")
    void failIfTagDoesNotExistWhenTagIsProvided() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + nickname;

        final String tag = FactoryPreset.TAG;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        doThrow(new TilException(TilStatus.NOT_EXISTING_TAG))
                .when(tilService)
                .readTilListByTag(nickname, tag, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "0")
                        .param("size", "5")
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(TilStatus.NOT_EXISTING_TAG.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TilStatus.NOT_EXISTING_TAG.getMessage());
    }

    @Test
    @DisplayName("[Success] tils do exist when tag is provided")
    void successIfTilsDoExistWhenTagIsProvided() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + nickname;

        final String tag = FactoryPreset.TAG;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final TilListReadResponseDto responseDto = TilListReadResponseDtoFactory.createDefaultTilListReadResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .readTilListByTag(nickname, tag, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "0")
                        .param("size", "5")
        );

        final TilListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTils().get(0).getUuid()).hasSize(36);
        assertThat(response.getTils().get(0).getTitle()).isEqualTo(responseDto.getTils().get(0).getTitle());
        assertThat(response.getTils().get(0).getContent()).isEqualTo(responseDto.getTils().get(0).getContent());
        assertThat(response.getTotalPages()).isEqualTo(responseDto.getTotalPages());
    }
}