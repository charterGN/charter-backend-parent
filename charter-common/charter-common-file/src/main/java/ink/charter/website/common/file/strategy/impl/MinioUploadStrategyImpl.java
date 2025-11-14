package ink.charter.website.common.file.strategy.impl;

import ink.charter.website.common.file.config.FileConfigProperties;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * MinIO上传策略实现类
 *
 * @author charter
 * @create 2025/11/14
 */
@Slf4j
@Component("minioUploadStrategyImpl")
public class MinioUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private FileConfigProperties fileConfigProperties;

    @Override
    public Boolean exists(String filePath) {
        try {
            MinioClient minioClient = getMinioClient();
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(fileConfigProperties.getBucketName())
                    .object(filePath)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        try {
            MinioClient minioClient = getMinioClient();
            
            // 检查桶是否存在
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(fileConfigProperties.getBucketName())
                    .build());
            
            if (!bucketExists) {
                log.warn("MinIO桶不存在: {}", fileConfigProperties.getBucketName());
            }
            
            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(fileConfigProperties.getBucketName())
                    .object(path + fileName)
                    .stream(inputStream, -1, 10485760)
                    .build());
                    
        } catch (Exception e) {
            log.error("MinIO文件上传失败", e);
            throw new RuntimeException("MinIO文件上传失败", e);
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return fileConfigProperties.getDomain() + "/" + filePath;
    }

    private MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(fileConfigProperties.getEndpoint())
                .credentials(fileConfigProperties.getAccessKeyId(), fileConfigProperties.getAccessKeySecret())
                .build();
    }

}
