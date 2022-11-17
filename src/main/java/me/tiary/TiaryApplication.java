package me.tiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TiaryApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TiaryApplication.class, args);
    }
}