package me.tiary.controller.tilcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.TilApiUrl;
import common.factory.dto.til.TilDeletionResponseDtoFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.TilController;
import me.tiary.dto.til.TilDeletionResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.TilStatus;
import me.tiary.security.web.userdetails.MemberDetails;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TilController] deleteTil")
class DeleteTilTest {
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

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_DELETION.getEntireUrl() + tilUuid;

        doThrow(new TilException(TilStatus.NOT_EXISTING_TIL))
                .when(tilService)
                .deleteTil(memberDetails.getProfileUuid(), tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
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

        final String url = TilApiUrl.TIL_DELETION.getEntireUrl() + tilUuid;

        doThrow(new TilException(TilStatus.NOT_AUTHORIZED_MEMBER))
                .when(tilService)
                .deleteTil(memberDetails.getProfileUuid(), tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
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
    @DisplayName("[Success] til is deleted")
    void successIfTilIsDeleted() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TilApiUrl.TIL_DELETION.getEntireUrl() + tilUuid;

        final TilDeletionResponseDto responseDto = TilDeletionResponseDtoFactory.createDefaultTilDeletionResponseDto();

        doReturn(responseDto)
                .when(tilService)
                .deleteTil(memberDetails.getProfileUuid(), tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
        );

        final TilDeletionResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TilDeletionResponseDto.class
        );

        // Then
        assertThat(response.getUuid()).hasSize(36);
    }
}