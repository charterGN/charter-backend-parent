package ink.charter.website.common.file.strategy.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import ink.charter.website.common.file.config.FileConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 七牛云Kodo上传策略实现类
 *
 * @author charter
 * @create 2025/11/14
 */
@Slf4j
@Component("qiniuUploadStrategyImpl")
public class QiniuUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private FileConfigProperties fileConfigProperties;

    @Override
    public Boolean exists(String filePath) {
        try {
            Auth auth = getAuth();
            Configuration cfg = new Configuration(Region.autoRegion());
            BucketManager bucketManager = new BucketManager(auth, cfg);
            
            bucketManager.stat(fileConfigProperties.getBucketName(), filePath);
            return true;
        } catch (QiniuException e) {
            if (e.response.statusCode == 612) {
                return false;
            }
            log.error("检查七牛云文件是否存在失败", e);
            return false;
        }
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        try {
            Auth auth = getAuth();
            String upToken = auth.uploadToken(fileConfigProperties.getBucketName());
            
            Configuration cfg = new Configuration(Region.autoRegion());
            UploadManager uploadManager = new UploadManager(cfg);
            
            Response response = uploadManager.put(inputStream, path + fileName, upToken, null, null);
            
            if (!response.isOK()) {
                throw new RuntimeException("七牛云文件上传失败: " + response.bodyString());
            }
            
        } catch (Exception e) {
            log.error("七牛云文件上传失败", e);
            throw new RuntimeException("七牛云文件上传失败", e);
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return fileConfigProperties.getDomain() + "/" + filePath;
    }

    private Auth getAuth() {
        return Auth.create(fileConfigProperties.getAccessKeyId(), fileConfigProperties.getAccessKeySecret());
    }

}
