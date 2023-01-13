package me.tiary.config.websecurityconfig;

import com.google.gson.Gson;
import common.annotation.application.ApplicationIntegrationTest;
import common.config.factory.FactoryPreset;
import common.config.url.AccountApiUrl;
import common.config.url.CommentApiUrl;
import common.config.url.ProfileApiUrl;
import common.config.url.ViewUrl;
import common.factory.dto.account.AccountCreationRequestDtoFactory;
import common.factory.dto.account.AccountLoginRequestDtoFactory;
import common.factory.dto.account.AccountVerificationRequestDtoFactory;
import common.factory.dto.comment.CommentWritingRequestDtoFactory;
import common.factory.dto.profile.ProfileCreationRequestDtoFactory;
import common.factory.dto.profile.ProfilePictureUploadRequestDtoFactory;
import common.factory.dto.profile.ProfileUpdateRequestDtoFactory;
import me.tiary.domain.Verification;
import me.tiary.dto.account.AccountCreationRequestDto;
import me.tiary.dto.account.AccountLoginRequestDto;
import me.tiary.dto.account.AccountVerificationRequestDto;
import me.tiary.dto.comment.CommentWritingRequestDto;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.dto.profile.ProfilePictureUploadRequestDto;
import me.tiary.dto.profile.ProfileUpdateRequestDto;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
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
    @DisplayName("[Success] member requests index view")
    void successIfMemberRequestsIndexView() throws Exception {
        // Given
        final String url = ViewUrl.INDEX.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Success] anonymous requests index view")
    void successIfAnonymousRequestsIndexView() throws Exception {
        // Given
        final String url = ViewUrl.INDEX.getEntireUrl();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] member requests login view")
    void failIfMemberRequestsLoginView() throws Exception {
        // Given
        final String url = ViewUrl.LOGIN.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Success] anonymous requests login view")
    void successIfAnonymousRequestsLoginView() throws Exception {
        // Given
        final String url = ViewUrl.LOGIN.getEntireUrl();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Success] member requests profile view")
    void successIfMemberRequestsProfileView() throws Exception {
        // Given
        final String url = ViewUrl.PROFILE.getEntireUrl() + FactoryPreset.NICKNAME;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Success] anonymous requests profile view")
    void successIfAnonymousRequestsProfileView() throws Exception {
        // Given
        final String url = ViewUrl.PROFILE.getEntireUrl() + FactoryPreset.NICKNAME;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] member requests email existence check api")
    void failIfMemberRequestsEmailExistenceCheckApi() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_EXISTENCE_CHECK.getEntireUrl() + FactoryPreset.EMAIL;

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
    @DisplayName("[Success] anonymous requests email existence check api")
    void successIfAnonymousRequestsEmailExistenceCheckApi() throws Exception {
        // Given
        final String url = AccountApiUrl.EMAIL_EXISTENCE_CHECK.getEntireUrl() + FactoryPreset.EMAIL;

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
    @DisplayName("[Fail] member requests login api")
    void failIfMemberRequestsLoginApi() throws Exception {
        // Given
        final String url = AccountApiUrl.LOGIN.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.createDefaultAccountLoginRequestDto();

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
    @DisplayName("[Success] anonymous requests login api")
    void successIfAnonymousRequestsLoginApi() throws Exception {
        // Given
        final String url = AccountApiUrl.LOGIN.getEntireUrl();

        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.createDefaultAccountLoginRequestDto();

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
    @DisplayName("[Success] member requests nickname existence check api")
    void successIfMemberRequestsNicknameExistenceCheckApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.NICKNAME_EXISTENCE_CHECK.getEntireUrl() + FactoryPreset.NICKNAME;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Success] anonymous requests nickname existence check api")
    void successIfAnonymousRequestsNicknameExistenceCheckApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.NICKNAME_EXISTENCE_CHECK.getEntireUrl() + FactoryPreset.NICKNAME;

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

    @Test
    @DisplayName("[Success] member requests profile read api")
    void successIfMemberRequestsProfileReadApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_READ.getEntireUrl() + FactoryPreset.NICKNAME;

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Success] anonymous requests profile read api")
    void successIfAnonymousRequestsProfileReadApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_READ.getEntireUrl() + FactoryPreset.NICKNAME;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Success] member requests profile update api")
    void successIfMemberRequestsProfileUpdateApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_UPDATE.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.createDefaultProfileUpdateRequestDto();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] anonymous requests profile update api")
    void failIfAnonymousRequestsProfileUpdateApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_UPDATE.getEntireUrl();

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.createDefaultProfileUpdateRequestDto();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Success] member requests profile picture upload api")
    void successIfMemberRequestsProfilePictureUploadApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_PICTURE_UPLOAD.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.createDefaultProfilePictureUploadRequestDto();

        // When
        final MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(url);

        builder.with(request -> {
            request.setMethod("PATCH");
            request.setCookies(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken));

            return request;
        });

        final ResultActions resultActions = mockMvc.perform(
                builder.file((MockMultipartFile) requestDto.getPictureFile())
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] anonymous requests profile picture upload api")
    void failIfAnonymousRequestsProfilePictureUploadApi() throws Exception {
        // Given
        final String url = ProfileApiUrl.PROFILE_PICTURE_UPLOAD.getEntireUrl();

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.createDefaultProfilePictureUploadRequestDto();

        // When
        final MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(url);

        builder.with(request -> {
            request.setMethod("PATCH");

            return request;
        });

        final ResultActions resultActions = mockMvc.perform(
                builder.file((MockMultipartFile) requestDto.getPictureFile())
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Success] member requests comment writing api")
    void successIfMemberRequestsCommentWritingApi() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    @DisplayName("[Fail] anonymous requests comment writing api")
    void failIfAnonymousRequestsCommentWritingApi() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }
}