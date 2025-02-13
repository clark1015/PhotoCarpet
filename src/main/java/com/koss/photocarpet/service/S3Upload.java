package com.koss.photocarpet.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class S3Upload {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return "empty";
        }
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
    public void fileDelete(String fileUrl) throws IOException {
        try{
            String fileKey = fileUrl.substring(58);
            String key = fileKey; // 폴더/파일.확장자
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();

            try {
                s3.deleteObject(bucket, "dbc02d7b-c429-4f70-af81-db46273d410e-2");
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }

            System.out.println(String.format("[%s] deletion complete", key));

        } catch (Exception exception) {
            throw new IOException("error");
        }
    }
}

