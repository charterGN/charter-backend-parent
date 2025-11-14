package ink.charter.website.common.file.strategy.context;

import cn.hutool.core.util.StrUtil;
import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import ink.charter.website.common.file.config.FileConfigProperties;
import ink.charter.website.common.file.enums.UploadModeEnum;
import ink.charter.website.common.file.mapper.SysFileConfigMapper;
import ink.charter.website.common.file.strategy.UploadStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * 上传文件策略上下文
 *
 * @author charter
 * @create 2025/07/16
 */
@Slf4j
@Service
public class UploadStrategyContext {

    @Autowired
    private FileConfigProperties fileConfigProperties;

    @Autowired
    private SysFileConfigMapper sysFileConfigMapper;

    @Autowired
    private Map<String, UploadStrategy> uploadStrategyMap;

    /**
     * 获取当前使用的存储类型
     * 优先使用数据库配置，如果没有则使用配置文件
     *
     * @return 存储类型
     */
    private String getCurrentStorageType() {
        // 优先从数据库获取启用的配置
        SysFileConfigEntity enabledConfig = sysFileConfigMapper.selectEnabledConfig();
        if (enabledConfig != null && StrUtil.isNotBlank(enabledConfig.getStorageType())) {
            log.debug("使用数据库配置的存储类型: {}", enabledConfig.getStorageType());
            // 同步配置到FileConfigProperties
            syncConfigFromDatabase(enabledConfig);
            return enabledConfig.getStorageType();
        }
        
        // 如果数据库没有配置，使用配置文件
        log.debug("使用配置文件的存储类型: {}", fileConfigProperties.getStorageType());
        return fileConfigProperties.getStorageType();
    }

    /**
     * 从数据库配置同步到FileConfigProperties
     *
     * @param config 数据库配置
     */
    private void syncConfigFromDatabase(SysFileConfigEntity config) {
        if (StrUtil.isNotBlank(config.getDomain())) {
            fileConfigProperties.setDomain(config.getDomain());
        }
        if (StrUtil.isNotBlank(config.getEndpoint())) {
            fileConfigProperties.setEndpoint(config.getEndpoint());
        }
        if (StrUtil.isNotBlank(config.getRegion())) {
            fileConfigProperties.setRegion(config.getRegion());
        }
        if (StrUtil.isNotBlank(config.getAccessKeyId())) {
            fileConfigProperties.setAccessKeyId(config.getAccessKeyId());
        }
        if (StrUtil.isNotBlank(config.getAccessKeySecret())) {
            fileConfigProperties.setAccessKeySecret(config.getAccessKeySecret());
        }
        if (StrUtil.isNotBlank(config.getBucketName())) {
            fileConfigProperties.setBucketName(config.getBucketName());
        }
    }

    public String executeUploadStrategy(MultipartFile file, String path) {
        String storageType = getCurrentStorageType();
        String strategyName = UploadModeEnum.getStrategy(storageType);
        
        if (StrUtil.isBlank(strategyName)) {
            log.error("未找到存储类型对应的策略: {}", storageType);
            throw new RuntimeException("不支持的存储类型: " + storageType);
        }
        
        UploadStrategy strategy = uploadStrategyMap.get(strategyName);
        if (strategy == null) {
            log.error("未找到上传策略实现: {}", strategyName);
            throw new RuntimeException("未找到上传策略实现: " + strategyName);
        }
        
        return strategy.uploadFile(file, path);
    }

    public String executeUploadStrategy(String fileName, InputStream inputStream, String path) {
        String storageType = getCurrentStorageType();
        String strategyName = UploadModeEnum.getStrategy(storageType);
        
        if (StrUtil.isBlank(strategyName)) {
            log.error("未找到存储类型对应的策略: {}", storageType);
            throw new RuntimeException("不支持的存储类型: " + storageType);
        }
        
        UploadStrategy strategy = uploadStrategyMap.get(strategyName);
        if (strategy == null) {
            log.error("未找到上传策略实现: {}", strategyName);
            throw new RuntimeException("未找到上传策略实现: " + strategyName);
        }
        
        return strategy.uploadFile(fileName, inputStream, path);
    }

}
