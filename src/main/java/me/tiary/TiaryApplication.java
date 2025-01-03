package me.tiary;

import me.tiary.properties.aws.AwsProperties;
import me.tiary.properties.aws.AwsS3Properties;
import me.tiary.properties.aws.AwsStorageProperties;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.properties.mail.MailProperties;
import me.tiary.properties.security.SecurityCorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
        AccessTokenProperties.class,
        RefreshTokenProperties.class,
        SecurityCorsProperties.class,
        AwsProperties.class,
        AwsS3Properties.class,
        AwsStorageProperties.class,
        MailProperties.class
})
@EnableJpaAuditing
@EnableScheduling
public class TiaryApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TiaryApplication.class, args);
    }
}