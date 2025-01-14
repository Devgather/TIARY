package me.tiary.controller.viewcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.factory.FactoryPreset;
import common.config.url.ViewUrl;
import me.tiary.controller.ViewController;
import me.tiary.domain.Profile;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.service.ProfileService;
import me.tiary.service.TilService;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(ViewController.class)
@DisplayName("[ViewController - Integration] directProfileView")
class DirectProfileViewIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private TilService tilService;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Fail] nickname is blank")
    void failIfNicknameIsBlank(final String tag) throws Exception {
        // Given
        final String url = ViewUrl.PROFILE.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "1")
                        .param("size", "5")
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Fail] nickname exceeds max length")
    void failIfNicknameExceedsMaxLength(final String tag) throws Exception {
        // Given
        final String nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH + 1);

        final String url = ViewUrl.PROFILE.getEntireUrl() + nickname;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "1")
                        .param("size", "5")
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Fail] nickname does not exist")
    void failIfNicknameDoesNotExist(final String tag) throws Exception {
        // Given
        final String url = ViewUrl.PROFILE.getEntireUrl() + FactoryPreset.NICKNAME;

        doReturn(false)
                .when(profileService)
                .checkNicknameExistence(FactoryPreset.NICKNAME);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "1")
                        .param("size", "5")
        );

        // Then
        resultActions.andExpect(status().isFound());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Success] anonymous views someone else profile")
    void successIfAnonymousViewsSomeoneElseProfile(final String tag) throws Exception {
        // Given
        final String url = ViewUrl.PROFILE.getEntireUrl() + FactoryPreset.NICKNAME;

        doReturn(true)
                .when(profileService)
                .checkNicknameExistence(FactoryPreset.NICKNAME);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "1")
                        .param("size", "5")
        );

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Success] member views someone else profile")
    void successIfMemberViewsSomeoneElseProfile(final String tag) throws Exception {
        // Given
        final String nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH);

        final String url = ViewUrl.PROFILE.getEntireUrl() + nickname;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        doReturn(true)
                .when(profileService)
                .checkNicknameExistence(nickname);

        doReturn(FactoryPreset.NICKNAME)
                .when(profileService)
                .searchNicknameUsingUuid("cbf0f220-97b8-4312-82ce-f98266c428d4");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "1")
                        .param("size", "5")
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Success] member views own profile")
    void successIfMemberViewsOwnProfile(final String tag) throws Exception {
        // Given
        final String url = ViewUrl.PROFILE.getEntireUrl() + FactoryPreset.NICKNAME;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        doReturn(true)
                .when(profileService)
                .checkNicknameExistence(FactoryPreset.NICKNAME);

        doReturn(FactoryPreset.NICKNAME)
                .when(profileService)
                .searchNicknameUsingUuid("cbf0f220-97b8-4312-82ce-f98266c428d4");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "1")
                        .param("size", "5")
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}