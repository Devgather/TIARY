package me.tiary.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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
    public AmazonS3 amazonS3(final AwsProperties awsProperties, final AwsS3Properties awsS3Properties) {
        final AWSCredentials credentials = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsS3Properties.getRegion())
                .build();
    }

    @Bean
    public AwsS3Manager awsS3Manager(final AmazonS3 amazonS3, final AwsS3Properties awsS3Properties) {
        return new AwsS3Manager(amazonS3, awsS3Properties.getBucket());
    }
}