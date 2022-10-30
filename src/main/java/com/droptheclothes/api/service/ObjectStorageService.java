package com.droptheclothes.api.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.droptheclothes.api.exception.ObjectStorageException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ObjectStorageService {

    private static final String END_POINT = "https://kr.object.ncloudstorage.com";

    private static final String REGION_NAME = "kr-standard";

    private static final String ACCESS_KEY = "rI6ncroRQ5tIBmIOSgNj";

    private static final String SECRET_KET = "lRtInG8nJFkIRO3mnNMRi0GHaZFkOZ0D3pNzNoiQ";

    private static final String BUCKET_NAME = "clothing-bin";

    public String uploadFileToObjectStorage(String directory, MultipartFile file) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                                                 .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(END_POINT, REGION_NAME))
                                                 .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KET)))
                                                 .build();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String uploadPathAndFileName = directory + "/" + uploadFileName;
        try {
            s3.putObject(BUCKET_NAME, uploadPathAndFileName, file.getInputStream(), objectMetadata);
        } catch (Exception e) {
            throw new ObjectStorageException(e.getMessage());
        }
        return uploadPathAndFileName;
    }
}
