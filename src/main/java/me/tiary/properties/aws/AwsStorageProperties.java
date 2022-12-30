package me.tiary.properties.aws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "aws.storage")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class AwsStorageProperties {
    private final String url;
}
