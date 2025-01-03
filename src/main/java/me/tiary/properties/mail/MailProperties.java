package me.tiary.properties.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "spring.mail")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class MailProperties {
    private final String username;
}