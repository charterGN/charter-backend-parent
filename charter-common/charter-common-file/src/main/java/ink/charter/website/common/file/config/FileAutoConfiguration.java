package ink.charter.website.common.file.config;

import ink.charter.website.common.file.mapper.SysFilesMapper;
import ink.charter.website.common.file.service.FileService;
import ink.charter.website.common.file.service.impl.FileServiceImpl;
import ink.charter.website.common.file.strategy.UploadStrategy;
import ink.charter.website.common.file.strategy.context.UploadStrategyContext;
import ink.charter.website.common.file.strategy.impl.OssUploadStrategyImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传自动配置类
 * 自动装配文件上传相关组件
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(MultipartFile.class)
public class FileAutoConfiguration {

    /**
     * 注册OSS配置属性
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "upload.oss", name = "endpoint")
    public OssConfigProperties ossConfigProperties() {
        log.info("初始化OSS配置属性");
        return new OssConfigProperties();
    }

    /**
     * 注册OSS上传策略实现
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "upload.oss", name = "endpoint")
    public OssUploadStrategyImpl ossUploadStrategyImpl(OssConfigProperties ossConfigProperties) {
        log.info("初始化OSS上传策略实现");
        return new OssUploadStrategyImpl();
    }

    /**
     * 注册上传策略上下文
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "upload", name = "mode")
    public UploadStrategyContext uploadStrategyContext() {
        log.info("初始化上传策略上下文");
        return new UploadStrategyContext();
    }

    /**
     * 注册文件服务
     */
    @Bean
    @ConditionalOnMissingBean
    public FileService fileService(SysFilesMapper sysFilesMapper, UploadStrategyContext uploadStrategyContext) {
        log.info("初始化文件服务");
        return new FileServiceImpl(sysFilesMapper, uploadStrategyContext);
    }
}