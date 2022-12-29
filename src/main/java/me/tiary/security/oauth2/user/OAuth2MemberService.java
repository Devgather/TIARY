package me.tiary.security.oauth2.user;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class OAuth2MemberService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User user = super.loadUser(userRequest);

        final Map<String, Object> attributes = user.getAttributes();

        final String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        final String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        return OAuth2Member.builder()
                .authorities(null)
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .registrationId(registrationId)
                .build();
    }
}