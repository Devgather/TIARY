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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

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
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() throws Exception {
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
    @DisplayName("[Success] tils do exist")
    void successIfTilsDoExist() throws Exception {
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
    }
}