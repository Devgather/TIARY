package me.tiary.config;

import me.tiary.properties.aws.AwsProperties;
import me.tiary.properties.aws.AwsS3Properties;
import me.tiary.utility.aws.AwsS3Manager;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        return modelMapper;
    }

    @Bean
    public AwsS3Manager awsS3Manager(final AwsProperties awsProperties, final AwsS3Properties awsS3Properties) {
        return new AwsS3Manager(awsProperties, awsS3Properties);
    }
}