package me.tiary.properties.aws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "aws")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class AwsProperties {
    private final String accessKey;

    private final String secretKey;
}
