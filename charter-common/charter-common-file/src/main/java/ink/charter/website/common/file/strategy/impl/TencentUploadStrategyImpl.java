package ink.charter.website.common.file.strategy.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import ink.charter.website.common.file.config.FileConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 腾讯云COS上传策略实现类
 *
 * @author charter
 * @create 2025/11/14
 */
@Slf4j
@Component("tencentUploadStrategyImpl")
public class TencentUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private FileConfigProperties fileConfigProperties;

    @Override
    public Boolean exists(String filePath) {
        COSClient cosClient = null;
        try {
            cosClient = getCosClient();
            return cosClient.doesObjectExist(fileConfigProperties.getBucketName(), filePath);
        } catch (Exception e) {
            log.error("检查腾讯云文件是否存在失败", e);
            return false;
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        COSClient cosClient = null;
        try {
            cosClient = getCosClient();
            
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(inputStream.available());
            
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    fileConfigProperties.getBucketName(),
                    path + fileName,
                    inputStream,
                    metadata
            );
            
            cosClient.putObject(putObjectRequest);
            
        } catch (Exception e) {
            log.error("腾讯云文件上传失败", e);
            throw new RuntimeException("腾讯云文件上传失败", e);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return fileConfigProperties.getDomain() + "/" + filePath;
    }

    private COSClient getCosClient() {
        COSCredentials cred = new BasicCOSCredentials(
                fileConfigProperties.getAccessKeyId(),
                fileConfigProperties.getAccessKeySecret()
        );
        
        Region region = new Region(fileConfigProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        
        return new COSClient(cred, clientConfig);
    }

}
