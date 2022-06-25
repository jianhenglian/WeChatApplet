package com.nju.nnt.common;

import com.nju.nnt.entity.User;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class MinIOUtils {

    private static final String BUCKET_PREFIX = "nnt";
    private static MinioClient minioClient;
    public static MinioClient getMinIOClient(){
        if(minioClient == null){
            minioClient =
                    MinioClient.builder()
                            .endpoint("http://139.196.157.116:9000")
                            .credentials("admin","12345678")
                            .build();
        }
        return minioClient;
    }
    public static String upLoadFile(MultipartFile file, User user) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinIOClient();
        String openId = user.getOpenId();
        long hashNum = openId.hashCode()%4 + 1;
        minioClient.putObject(PutObjectArgs.builder().bucket(BUCKET_PREFIX+hashNum)
                .object(file.getOriginalFilename())
                .stream(file.getInputStream(), file.getInputStream().available(),-1)
                .build());
       return  BUCKET_PREFIX+hashNum+"/"+file.getOriginalFilename();
    }
    public static InputStream getImageStream(String bucketName,String imagePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinIOClient();
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName)
                .object(imagePath).build());
    }
}
