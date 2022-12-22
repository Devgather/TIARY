package me.tiary.config.websecurityconfig;

import annotation.application.ApplicationIntegrationTest;
import com.google.gson.Gson;
import config.factory.FactoryPreset;
import config.url.AccountApiUrl;
import config.url.ProfileApiUrl;
import factory.dto.account.AccountCreationRequestDtoFactory;
import factory.dto.account.AccountVerificationRequestDtoFactory;
import factory.dto.profile.ProfileCreationRequestDtoFactory;
import me.tiary.domain.Verification;
import me.tiary.dto.account.AccountCreationRequestDto;
import me.tiary.dto.account.AccountVerificationRequestDto;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;
import java.util.UUID;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ApplicationIntegrationTest
@DisplayName("[WebSecurityConfig - Integration] securityFilterChain")
class SecurityFilterChainIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        gson = new Gson();
    }

    @Test
    @DisplayName("[Fail] member requests email duplication check api")
    void failIfMemberRequestsEmailDuplicationCheckApi() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_DUPLICATION_CHECK.getEntireUrl() + FactoryPreset.EMAIL;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Success] anonymous requests email duplication check api")
    void successIfAnonymousRequestsEmailDuplicationCheckApi() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_DUPLICATION_CHECK.getEntireUrl() + FactoryPreset.EMAIL;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] member requests register api")
    void failIfMemberRequestsRegisterApi() throws Exception {
        // Given
        final String url = AccountApiUrl.REGISTER.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Success] anonymous requests register api")
    void successIfAnonymousRequestsRegisterApi() throws Exception {
        // Given
        final String url = AccountApiUrl.REGISTER.getEntireUrl();

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] member requests verification mail delivery api")
    void failIfMemberRequestsVerificationMailDeliveryApi() throws Exception {
        // Given
        final String url = AccountApiUrl.VERIFICATION_MAIL_DELIVERY.getEntireUrl() + FactoryPreset.EMAIL;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Success] anonymous requests verification mail delivery api")
    void successIfAnonymousRequestsVerificationMailDeliveryApi() throws Exception {
        // Given
        final String url = AccountApiUrl.VERIFICATION_MAIL_DELIVERY.getEntireUrl() + FactoryPreset.EMAIL;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] member requests email verification api")
    void failIfMemberRequestsEmailVerificationApi() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Success] anonymous requests email verification api")
    void successIfAnonymousRequestsEmailVerificationApi() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_VERIFICATION.getEntireUrl();

        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] member requests nickname duplication check api")
    void failIfMemberRequestsNicknameDuplicationCheckApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.NICKNAME_DUPLICATION_CHECK.getEntireUrl() + FactoryPreset.NICKNAME;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Success] anonymous requests nickname duplication check api")
    void successIfAnonymousRequestsNicknameDuplicationCheckApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.NICKNAME_DUPLICATION_CHECK.getEntireUrl() + FactoryPreset.NICKNAME;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] member requests profile creation api")
    void failIfMemberRequestsProfileCreationApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_CREATION.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.createDefaultProfileCreationRequestDto();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Success] anonymous requests profile creation api")
    void successIfAnonymousRequestsProfileCreationApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_CREATION.getEntireUrl();

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.createDefaultProfileCreationRequestDto();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }
}