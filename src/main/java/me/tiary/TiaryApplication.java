package me.tiary;

import me.tiary.properties.security.SecurityCorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties({SecurityCorsProperties.class})
@EnableJpaAuditing
public class TiaryApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TiaryApplication.class, args);
    }
}