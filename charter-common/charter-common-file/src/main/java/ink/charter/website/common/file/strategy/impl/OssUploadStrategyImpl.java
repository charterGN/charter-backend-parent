package ink.charter.website.common.file.strategy.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import ink.charter.website.common.file.config.FileConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 阿里云oss上传策略实现类
 *
 * @author charter
 * @create 2025/07/16
 */
@Component("ossUploadStrategyImpl")
public class OssUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private FileConfigProperties fileConfigProperties;

    @Override
    public Boolean exists(String filePath) {
        return getOssClient().doesObjectExist(fileConfigProperties.getBucketName(), filePath);
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        getOssClient().putObject(fileConfigProperties.getBucketName(), path + fileName, inputStream);
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return fileConfigProperties.getDomain() + "/" + filePath;
    }

    private OSS getOssClient() {
        return new OSSClientBuilder().build(fileConfigProperties.getEndpoint(), fileConfigProperties.getAccessKeyId(), fileConfigProperties.getAccessKeySecret());
    }

}
