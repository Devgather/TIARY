package me.tiary.properties.aws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "aws.s3")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class AwsS3Properties {
    private final String region;

    private final String bucket;
}
