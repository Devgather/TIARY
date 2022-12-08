package me.tiary.utility.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import me.tiary.properties.aws.AwsProperties;
import me.tiary.properties.aws.AwsS3Properties;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AwsS3Manager {
    private final AmazonS3 amazonS3;

    private final String bucket;

    public AwsS3Manager(final AwsProperties awsProperties, final AwsS3Properties awsS3Properties) {
        final AWSCredentials awsCredentials = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(awsS3Properties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        this.bucket = awsS3Properties.getBucket();
    }

    public List<String> uploadFiles(final Function<String, String> titleGenerator, final List<MultipartFile> multipartFile) {
        final List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            final String originalFileName = file.getOriginalFilename();
            final String fileName = titleGenerator.apply(originalFileName);

            final ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(final InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(final IOException e) {
                throw new IllegalArgumentException();
            }

            fileNameList.add(fileName);
        });

        return fileNameList;
    }
}
