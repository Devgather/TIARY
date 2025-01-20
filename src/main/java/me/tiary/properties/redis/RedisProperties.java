package me.tiary.properties.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "redis")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class RedisProperties {
    private final String host;

    private final int port;

    private final long ttlSeconds;
}