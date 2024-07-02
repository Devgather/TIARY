package me.tiary.controller.tilcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.TilApiUrl;
import common.factory.dto.til.TilReadResponseDtoFactory;
import common.utility.GsonLocalDateTimeDeserializer;
import common.utility.GsonLocalDateTimeSerializer;
import me.tiary.controller.TilController;
import me.tiary.dto.til.TilReadResponseDto;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TilController] readTil")
class ReadTilTest {
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

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeDeserializer());

        gson = gsonBuilder.create();
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_READ.getEntireUrl() + tilUuid;

        doThrow(new TilException(TilStatus.NOT_EXISTING_TIL))
                .when(tilService)
                .readTil(tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
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
    @DisplayName("[Success] til does exist and tags do not exist")
    void successIfTilDoesExistAndTagsDoNotExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_READ.getEntireUrl() + tilUuid;

        final TilReadResponseDto responseDto = TilReadResponseDtoFactory.create(
                FactoryPreset.TITLE, FactoryPreset.CONTENT, FactoryPreset.MARKDOWN, new ArrayList<>(), FactoryPreset.NICKNAME, LocalDateTime.now()
        );

        doReturn(responseDto)
                .when(tilService)
                .readTil(tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        final TilReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(response.getContent()).isEqualTo(responseDto.getContent());
        assertThat(response.getMarkdown()).isEqualTo(responseDto.getMarkdown());
        assertThat(response.getTags()).isEmpty();
        assertThat(response.getAuthor()).isEqualTo(responseDto.getAuthor());
        assertThat(response.getCreatedDate()).isEqualTo(responseDto.getCreatedDate().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("[Success] til does exist and tags do exist")
    void successIfTilDoesExistAndTagsDoExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_READ.getEntireUrl() + tilUuid;

        final TilReadResponseDto responseDto = TilReadResponseDtoFactory.createDefaultTilReadResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .readTil(tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        final TilReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(response.getContent()).isEqualTo(responseDto.getContent());
        assertThat(response.getMarkdown()).isEqualTo(responseDto.getMarkdown());
        assertThat(response.getTags()).hasSize(responseDto.getTags().size());
        assertThat(response.getAuthor()).isEqualTo(responseDto.getAuthor());
        assertThat(response.getCreatedDate()).isEqualTo(responseDto.getCreatedDate().truncatedTo(ChronoUnit.SECONDS));
    }
}