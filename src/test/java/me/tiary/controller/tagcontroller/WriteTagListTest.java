package me.tiary.controller.tagcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.TagApiUrl;
import common.factory.dto.tag.TagListWritingRequestDtoFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.TagController;
import me.tiary.dto.tag.TagListWritingRequestDto;
import me.tiary.exception.TagException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.TagStatus;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TagService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TagController] writeTagList")
class WriteTagListTest {
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
    @DisplayName("[Fail] tags are empty")
    void failIfTagsAreEmpty() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TagApiUrl.TAG_LIST_WRITING.getEntireUrl() + tilUuid;

        final TagListWritingRequestDto requestDto = TagListWritingRequestDtoFactory.create(null);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("[Fail] tag is invalid")
    void failIfTagIsInvalid(final String tag) throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TagApiUrl.TAG_LIST_WRITING.getEntireUrl() + tilUuid;

        final List<String> tags = new ArrayList<>(FactoryPreset.TAGS);
        tags.add(tag);

        final TagListWritingRequestDto requestDto = TagListWritingRequestDtoFactory.create(tags);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TagApiUrl.TAG_LIST_WRITING.getEntireUrl() + tilUuid;

        final TagListWritingRequestDto requestDto = TagListWritingRequestDtoFactory.createDefaultTagListWritingRequestDto();

        doThrow(new TagException(TagStatus.NOT_EXISTING_TIL))
                .when(tagService)
                .writeTagList(memberDetails.getProfileUuid(), tilUuid, requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
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
        resultActions.andExpect(status().is(TagStatus.NOT_EXISTING_TIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TagStatus.NOT_EXISTING_TIL.getMessage());
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TagApiUrl.TAG_LIST_WRITING.getEntireUrl() + tilUuid;

        final TagListWritingRequestDto requestDto = TagListWritingRequestDtoFactory.createDefaultTagListWritingRequestDto();

        doThrow(new TagException(TagStatus.NOT_AUTHORIZED_MEMBER))
                .when(tagService)
                .writeTagList(memberDetails.getProfileUuid(), tilUuid, requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
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
        resultActions.andExpect(status().is(TagStatus.NOT_AUTHORIZED_MEMBER.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TagStatus.NOT_AUTHORIZED_MEMBER.getMessage());
    }

    @Test
    @DisplayName("[Success] tags are acceptable")
    void successIfTagsAreAcceptable() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = TagApiUrl.TAG_LIST_WRITING.getEntireUrl() + tilUuid;

        final TagListWritingRequestDto requestDto = TagListWritingRequestDtoFactory.createDefaultTagListWritingRequestDto();

        doNothing()
                .when(tagService)
                .writeTagList(memberDetails.getProfileUuid(), tilUuid, requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isCreated());
    }
}