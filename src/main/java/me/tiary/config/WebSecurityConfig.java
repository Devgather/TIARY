package me.tiary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.tiary.exception.handler.security.AccessDeniedExceptionHandler;
import me.tiary.exception.handler.security.AuthenticationExceptionHandler;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.properties.security.SecurityCorsProperties;
import me.tiary.security.authentication.MemberAuthenticationConverter;
import me.tiary.security.authentication.MemberAuthenticationProvider;
import me.tiary.security.userdetails.MemberDetailsService;
import me.tiary.utility.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final AuthenticationEntryPoint authenticationEntryPoint,
                                                   final AccessDeniedHandler accessDeniedHandler,
                                                   final CorsConfigurationSource corsConfigurationSource,
                                                   final AuthenticationFilter authenticationFilter) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.HEAD, "/api/account/email/**").anonymous()
                .antMatchers(HttpMethod.POST, "/api/account").anonymous()
                .antMatchers(HttpMethod.POST, "/api/account/login").anonymous()
                .antMatchers(HttpMethod.HEAD, "/api/profile/nickname/**").anonymous()
                .antMatchers(HttpMethod.POST, "/api/profile").anonymous()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .rememberMe().disable()
                .headers().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(PathRequest.toH2Console());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(final ObjectMapper objectMapper) {
        return new AuthenticationExceptionHandler(objectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(final ObjectMapper objectMapper) {
        return new AccessDeniedExceptionHandler(objectMapper);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(final SecurityCorsProperties properties) {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(properties.isAllowCredentials());
        corsConfiguration.setAllowedHeaders(properties.getAllowedHeaders());
        corsConfiguration.setAllowedMethods(properties.getAllowedMethods());
        corsConfiguration.setAllowedOrigins(properties.getAllowedOrigins());
        corsConfiguration.setMaxAge(corsConfiguration.getMaxAge());

        final UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsConfigurationSource;
    }

    @Bean
    public AuthenticationFilter authenticationFilter(final AuthenticationManager authenticationManager,
                                                     final AuthenticationConverter authenticationConverter,
                                                     final AuthenticationSuccessHandler authenticationSuccessHandler,
                                                     final AuthenticationFailureHandler authenticationFailureHandler) {
        final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, authenticationConverter);

        authenticationFilter.setSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setFailureHandler(authenticationFailureHandler);

        return authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationConverter authenticationConverter() {
        return new MemberAuthenticationConverter();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(final AuthenticationEntryPoint authenticationEntryPoint) {
        return new AuthenticationEntryPointFailureHandler(authenticationEntryPoint);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(final AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService) {
        return new MemberAuthenticationProvider(authenticationUserDetailsService);
    }

    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService(final @Qualifier("accessTokenProvider") JwtProvider accessTokenProvider) {
        return new MemberDetailsService(accessTokenProvider);
    }

    @Bean(name = "accessTokenProvider")
    public JwtProvider accessTokenProvider(final AccessTokenProperties properties) {
        return new JwtProvider(properties);
    }

    @Bean(name = "refreshTokenProvider")
    public JwtProvider refreshTokenProvider(final RefreshTokenProperties properties) {
        return new JwtProvider(properties);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}