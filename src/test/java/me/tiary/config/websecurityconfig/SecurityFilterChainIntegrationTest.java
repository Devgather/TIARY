package me.tiary.config.websecurityconfig;

import com.google.gson.Gson;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.properties.jwt.AccessTokenProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(H2ConsoleProperties.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureMockMvc
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
        final String url = "/api/account/email/test@example.com";

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = Test
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.G0z3gVEh_uwH0cq0stN6JE7PkwC8L4DwzginXwX-1qg";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Success] anonymous requests email duplication check api")
    void successIfAnonymousRequestsEmailDuplicationCheckApi() throws Exception {
        // Given
        final String url = "/api/account/email/test@example.com";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Fail] member requests nickname duplication check api")
    void failIfMemberRequestsNicknameDuplicationCheckApi() throws Exception {
        // Given
        final String url = "/api/profile/nickname/Test";

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = Test
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.G0z3gVEh_uwH0cq0stN6JE7PkwC8L4DwzginXwX-1qg";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Success] anonymous requests nickname duplication check api")
    void successIfAnonymousRequestsNicknameDuplicationCheckApi() throws Exception {
        // Given
        final String url = "/api/profile/nickname/Test";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Fail] member requests profile creation api")
    void failIfMemberRequestsProfileCreationApi() throws Exception {
        // Given
        final String url = "/api/profile";

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = Test
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.G0z3gVEh_uwH0cq0stN6JE7PkwC8L4DwzginXwX-1qg";

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDto.builder()
                .nickname("Test")
                .build();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDto))
        );

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Success] anonymous requests profile creation api")
    void successIfAnonymousRequestsProfileCreationApi() throws Exception {
        // Given
        final String url = "/api/profile";

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDto.builder()
                .nickname("Test")
                .build();

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