package me.tiary.utility.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class AwsS3Manager {
    private final AmazonS3 amazonS3;

    private final String bucket;

    public List<String> uploadFiles(final Function<String, String> titleGenerator, final List<MultipartFile> multipartFile) {
        final List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            final String originalFileName = file.getOriginalFilename();
            final String fileName = titleGenerator.apply(originalFileName);

            final ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (final InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (final IOException e) {
                throw new IllegalArgumentException();
            }

            fileNameList.add(fileName);
        });

        return fileNameList;
    }
}
