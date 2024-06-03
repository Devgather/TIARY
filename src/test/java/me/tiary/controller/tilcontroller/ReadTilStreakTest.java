package me.tiary.controller.tilcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.TilApiUrl;
import common.factory.dto.til.TilStreakReadResponseDtoFactory;
import common.utility.GsonLocalDateDeserializer;
import common.utility.GsonLocalDateSerializer;
import me.tiary.controller.TilController;
import me.tiary.dto.til.TilStreakReadResponseDto;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TilController] readTilStreak")
class ReadTilStreakTest {
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

        final GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new GsonLocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new GsonLocalDateDeserializer());

        gson = gsonBuilder.create();
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_STREAK_READ.getEntireUrl() + nickname;

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = LocalDate.now();

        doThrow(new TilException(TilStatus.NOT_EXISTING_PROFILE))
                .when(tilService)
                .readTilStreak(nickname, startDate, endDate);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
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
    @DisplayName("[Success] tils do not exist")
    void successIfTilsDoNotExist() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_STREAK_READ.getEntireUrl() + nickname;

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = LocalDate.now();

        final TilStreakReadResponseDto responseDto = TilStreakReadResponseDtoFactory.create(new ArrayList<>());

        doReturn(responseDto)
                .when(tilService)
                .readTilStreak(nickname, startDate, endDate);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
        );

        final TilStreakReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilStreakReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getStreaks()).isEmpty();
    }

    @Test
    @DisplayName("[Success] tils do exist")
    void successIfTilsDoExist() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TilApiUrl.TIL_STREAK_READ.getEntireUrl() + nickname;

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = LocalDate.now();

        final TilStreakReadResponseDto responseDto = TilStreakReadResponseDtoFactory.createDefaultTilStreakReadResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .readTilStreak(nickname, startDate, endDate);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
        );

        final TilStreakReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilStreakReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getStreaks().get(0).getDate()).isEqualTo(responseDto.getStreaks().get(0).getDate());
        assertThat(response.getStreaks().get(0).getTilNumber()).isEqualTo(responseDto.getStreaks().get(0).getTilNumber());
    }
}