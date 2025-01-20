package me.tiary.config;

import lombok.RequiredArgsConstructor;
import me.tiary.properties.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.Duration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public CacheManager redisCacheManager(final RedisConnectionFactory redisConnectionFactory) {
        final RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofSeconds(redisProperties.getTtlSeconds()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    @Profile("local")
    public EmbeddedRedisServer embeddedRedisServer() {
        return new EmbeddedRedisServer(redisProperties);
    }

    public static class EmbeddedRedisServer implements SmartLifecycle {
        private final RedisServer redisServer;

        public EmbeddedRedisServer(final RedisProperties redisProperties) {
            try {
                this.redisServer = RedisServer.newRedisServer()
                        .bind(redisProperties.getHost())
                        .port(redisProperties.getPort())
                        .build();
            } catch (final IOException ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void start() {
            try {
                redisServer.start();
            } catch (final Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void stop() {
            try {
                redisServer.stop();
            } catch (final Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public boolean isRunning() {
            if (redisServer == null) {
                return false;
            }

            return redisServer.isActive();
        }
    }
}