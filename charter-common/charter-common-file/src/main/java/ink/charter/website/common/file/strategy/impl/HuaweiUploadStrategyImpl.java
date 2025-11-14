package ink.charter.website.common.file.strategy.impl;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import ink.charter.website.common.file.config.FileConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 华为云OBS上传策略实现类
 *
 * @author charter
 * @create 2025/11/14
 */
@Slf4j
@Component("huaweiUploadStrategyImpl")
public class HuaweiUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private FileConfigProperties fileConfigProperties;

    @Override
    public Boolean exists(String filePath) {
        try {
            ObsClient obsClient = getObsClient();
            boolean exists = obsClient.doesObjectExist(fileConfigProperties.getBucketName(), filePath);
            obsClient.close();
            return exists;
        } catch (Exception e) {
            log.error("检查华为云文件是否存在失败", e);
            return false;
        }
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        try (ObsClient obsClient = getObsClient()) {

            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(fileConfigProperties.getBucketName());
            request.setObjectKey(path + fileName);
            request.setInput(inputStream);

            obsClient.putObject(request);

        } catch (Exception e) {
            log.error("华为云文件上传失败", e);
            throw new RuntimeException("华为云文件上传失败", e);
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return fileConfigProperties.getDomain() + "/" + filePath;
    }

    private ObsClient getObsClient() {
        return new ObsClient(
                fileConfigProperties.getAccessKeyId(),
                fileConfigProperties.getAccessKeySecret(),
                fileConfigProperties.getEndpoint()
        );
    }

}
