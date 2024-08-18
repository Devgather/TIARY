package me.tiary.controller.tagcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.TagApiUrl;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.TagController;
import me.tiary.exception.TagException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.TagStatus;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TagService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TagController] deleteTagList")
class DeleteTagListTest {
    @InjectMocks
    private TagController tagController;

    @Mock
    private TagService tagService;

    private MemberDetails memberDetails;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        memberDetails = MemberDetails.builder()
                .profileUuid(UUID.randomUUID().toString())
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(tagController)
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

        final String url = TagApiUrl.TAG_LIST_DELETION.getEntireUrl() + tilUuid;

        doThrow(new TagException(TagStatus.NOT_EXISTING_TIL))
                .when(tagService)
                .deleteTagList(memberDetails.getProfileUuid(), tilUuid);

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
        resultActions.andExpect(status().is(TagStatus.NOT_EXISTING_TIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TagStatus.NOT_EXISTING_TIL.getMessage());
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TagApiUrl.TAG_LIST_DELETION.getEntireUrl() + tilUuid;

        doThrow(new TagException(TagStatus.NOT_AUTHORIZED_MEMBER))
                .when(tagService)
                .deleteTagList(memberDetails.getProfileUuid(), tilUuid);

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
        resultActions.andExpect(status().is(TagStatus.NOT_AUTHORIZED_MEMBER.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TagStatus.NOT_AUTHORIZED_MEMBER.getMessage());
    }

    @Test
    @DisplayName("[Success] tags are deleted")
    void successIfTagsAreDeleted() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TagApiUrl.TAG_LIST_DELETION.getEntireUrl() + tilUuid;

        doNothing()
                .when(tagService)
                .deleteTagList(memberDetails.getProfileUuid(), tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
        );

        // Then
        resultActions.andExpect(status().isOk());
    }
}