package me.tiary.common.config;

import me.tiary.common.filter.AccessLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AccessLoggingFilter> accessLoggingFilter() {
        final FilterRegistrationBean<AccessLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AccessLoggingFilter());
        registration.setOrder(1);
        registration.addUrlPatterns("/*");

        return registration;
    }

}
