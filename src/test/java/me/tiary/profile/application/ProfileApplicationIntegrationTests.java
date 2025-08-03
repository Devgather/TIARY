package me.tiary.profile.application;

import me.tiary.common.dto.ApiResponse;
import me.tiary.profile.controller.dto.NicknameCheckResponse;
import me.tiary.profile.controller.dto.NicknameCheckResponseApiResponseFixture;
import me.tiary.profile.domain.ProfileFixture;
import me.tiary.profile.repository.ProfileRepository;
import me.tiary.support.annotation.ApplicationIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ApplicationIntegrationTest
class ProfileApplicationIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    MessageSource messageSource;

    @Nested
    class CheckNicknameTest {

        static final String URL = "/api/profiles/nickname/check";

        static final String NICKNAME_PARAM = "nickname";

        @Test
        void shouldReturnUnavailable_whenNicknameIsUnavailable() throws Exception {
            // Given
            profileRepository.save(ProfileFixture.create());

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
