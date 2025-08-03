package me.tiary.profile.controller;

import me.tiary.common.dto.ApiResponse;
import me.tiary.common.dto.ExceptionResponse;
import me.tiary.common.dto.ExceptionResponseFixture;
import me.tiary.profile.controller.dto.NicknameCheckResponse;
import me.tiary.profile.controller.dto.NicknameCheckResponseApiResponseFixture;
import me.tiary.profile.domain.Profile;
import me.tiary.profile.domain.ProfileFixture;
import me.tiary.profile.service.ProfileService;
import me.tiary.support.annotation.ControllerIntegrationTest;
import me.tiary.support.util.lang.StringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(ProfileController.class)
class ProfileControllerIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProfileService profileService;

    @Autowired
    MessageSource messageSource;

    @Nested
    class CheckNicknameTest {

        static final String URL = "/api/profiles/nickname/check";

        static final String NICKNAME_PARAM = "nickname";

        @Test
        void shouldReturnBadRequest_whenNicknameIsMissing() throws Exception {
            // When
            ResultActions resultActions = mockMvc.perform(get(URL));

            String responseContent = resultActions.andReturn()
                    .getResponse()
                    .getContentAsString();

            ExceptionResponse response = ExceptionResponseFixture.from(responseContent);

            // Then
            resultActions.andExpect(status().isBadRequest());

            assertSoftly(softly -> {
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(messageSource.getMessage("required", new Object[]{"nickname"}, null));
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

        @Test
        void shouldReturnBadRequest_whenNicknameIsEmpty() throws Exception {
            // When
            ResultActions resultActions = mockMvc.perform(get(URL).param(NICKNAME_PARAM, ""));

            String responseContent = resultActions.andReturn()
                    .getResponse()
                    .getContentAsString();

            ExceptionResponse response = ExceptionResponseFixture.from(responseContent);

            // Then
            resultActions.andExpect(status().isBadRequest());

            assertSoftly(softly -> {
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(messageSource.getMessage("notblank.profile.nickname", null, null));
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

        @Test
        void shouldReturnBadRequest_whenNicknameIsBlank() throws Exception {
            // When
            ResultActions resultActions = mockMvc.perform(get(URL).param(NICKNAME_PARAM, " "));

            String responseContent = resultActions.andReturn()
                    .getResponse()
                    .getContentAsString();

            ExceptionResponse response = ExceptionResponseFixture.from(responseContent);

            // Then
            resultActions.andExpect(status().isBadRequest());

            assertSoftly(softly -> {
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(messageSource.getMessage("notblank.profile.nickname", null, null));
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

        @Test
        void shouldReturnBadRequest_whenNicknameExceedsMaxLength() throws Exception {
            // When
            ResultActions resultActions = mockMvc.perform(get(URL).param(NICKNAME_PARAM, StringUtils.generateRandomString(Profile.MAX_NICKNAME_LENGTH + 1)));

            String responseContent = resultActions.andReturn()
                    .getResponse()
                    .getContentAsString();

            ExceptionResponse response = ExceptionResponseFixture.from(responseContent);

            // Then
            resultActions.andExpect(status().isBadRequest());

            assertSoftly(softly -> {
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(messageSource.getMessage("size.profile.nickname", null, null).replace("{max}", String.valueOf(Profile.MAX_NICKNAME_LENGTH)));
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

        @Test
        void shouldReturnUnavailable_whenNicknameIsUnavailable() throws Exception {
            // Given
            given(profileService.isNicknameAvailable(ProfileFixture.DEFAULT_NICKNAME))
                    .willReturn(false);

            // When
            ResultActions resultActions = mockMvc.perform(get(URL).param(NICKNAME_PARAM, ProfileFixture.DEFAULT_NICKNAME));

            String responseContent = resultActions.andReturn()
                    .getResponse()
                    .getContentAsString();

            ApiResponse<NicknameCheckResponse> response = NicknameCheckResponseApiResponseFixture.from(responseContent);

            // Then
            resultActions.andExpect(status().isOk());

            assertSoftly(softly -> {
                softly.assertThat(response.data().available()).isFalse();
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(messageSource.getMessage("unavailable.profile.nickname", null, null));
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

        @Test
        void shouldReturnAvailable_whenNicknameIsAvailable() throws Exception {
            // Given
            given(profileService.isNicknameAvailable(ProfileFixture.DEFAULT_NICKNAME))
                    .willReturn(true);

            // When
            ResultActions resultActions = mockMvc.perform(get(URL).param(NICKNAME_PARAM, ProfileFixture.DEFAULT_NICKNAME));

            String responseContent = resultActions.andReturn()
                    .getResponse()
                    .getContentAsString();

            ApiResponse<NicknameCheckResponse> response = NicknameCheckResponseApiResponseFixture.from(responseContent);

            // Then
            resultActions.andExpect(status().isOk());

            assertSoftly(softly -> {
                softly.assertThat(response.data().available()).isTrue();
                softly.assertThat(response.messages()).hasSize(1);
                softly.assertThat(response.messages().get(0)).isEqualTo(messageSource.getMessage("available.profile.nickname", null, null));
                softly.assertThat(response.timestamp()).isNotNull();
            });
        }

    }

}
