package me.tiary.controller.tilcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.TilApiUrl;
import common.factory.dto.til.RecentTilListReadResponseDtoFactory;
import me.tiary.controller.TilController;
import me.tiary.dto.til.RecentTilListReadResponseDto;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TilController] readRecentTilList")
class ReadRecentTilListTest {
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
    @DisplayName("[Success] tils do not exist")
    void successIfTilsDoNotExist() throws Exception {
        // Given
        final String url = TilApiUrl.RECENT_TIL_LIST_READ.getEntireUrl();

        final Pageable pageable = PageRequest.of(0, 3, Sort.by("createdDate").descending());

        final RecentTilListReadResponseDto responseDto = RecentTilListReadResponseDtoFactory.create(new ArrayList<>());

        doReturn(responseDto)
                .when(tilService)
                .readRecentTilList(pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("size", "3")
        );

        final RecentTilListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                RecentTilListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTils()).isEmpty();
    }

    @Test
    @DisplayName("[Success] tils do exist")
    void successIfTilsDoExist() throws Exception {
        // Given
        final String url = TilApiUrl.RECENT_TIL_LIST_READ.getEntireUrl();

        final Pageable pageable = PageRequest.of(0, 3, Sort.by("createdDate").descending());

        final RecentTilListReadResponseDto responseDto = RecentTilListReadResponseDtoFactory.createDefaultRecentTilListReadResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .readRecentTilList(pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("size", "3")
        );

        final RecentTilListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                RecentTilListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTils().get(0).getUuid()).hasSize(36);
        assertThat(response.getTils().get(0).getNickname()).isEqualTo(responseDto.getTils().get(0).getNickname());
        assertThat(response.getTils().get(0).getPicture()).isEqualTo(responseDto.getTils().get(0).getPicture());
        assertThat(response.getTils().get(0).getTitle()).isEqualTo(responseDto.getTils().get(0).getTitle());
        assertThat(response.getTils().get(0).getContent()).isEqualTo(responseDto.getTils().get(0).getContent());
    }
}
