package ink.charter.website.common.file.config;

import ink.charter.website.common.file.mapper.SysFilesMapper;
import ink.charter.website.common.file.service.FileService;
import ink.charter.website.common.file.service.impl.FileServiceImpl;
import ink.charter.website.common.file.strategy.context.UploadStrategyContext;
import ink.charter.website.common.file.strategy.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
     * 注册文件配置属性
     */
    @Bean
    @ConditionalOnMissingBean
    public FileConfigProperties fileConfigProperties() {
        log.info("初始化文件配置属性");
        return new FileConfigProperties();
    }

    /**
     * 注册阿里云OSS上传策略
     * 只有在阿里云OSS SDK存在时才创建
     */
    @Bean("ossUploadStrategyImpl")
    @ConditionalOnClass(name = "com.aliyun.oss.OSS")
    @ConditionalOnMissingBean(name = "ossUploadStrategyImpl")
    public OssUploadStrategyImpl ossUploadStrategyImpl() {
        log.info("初始化阿里云OSS上传策略");
        return new OssUploadStrategyImpl();
    }

    /**
     * 注册MinIO上传策略
     * 只有在MinIO SDK存在时才创建
     */
    @Bean("minioUploadStrategyImpl")
    @ConditionalOnClass(name = "io.minio.MinioClient")
    @ConditionalOnMissingBean(name = "minioUploadStrategyImpl")
    public MinioUploadStrategyImpl minioUploadStrategyImpl() {
        log.info("初始化MinIO上传策略");
        return new MinioUploadStrategyImpl();
    }

    /**
     * 注册华为云OBS上传策略
     * 只有在华为云OBS SDK存在时才创建
     */
    @Bean("huaweiUploadStrategyImpl")
    @ConditionalOnClass(name = "com.obs.services.ObsClient")
    @ConditionalOnMissingBean(name = "huaweiUploadStrategyImpl")
    public HuaweiUploadStrategyImpl huaweiUploadStrategyImpl() {
        log.info("初始化华为云OBS上传策略");
        return new HuaweiUploadStrategyImpl();
    }

    /**
     * 注册腾讯云COS上传策略
     * 只有在腾讯云COS SDK存在时才创建
     */
    @Bean("tencentUploadStrategyImpl")
    @ConditionalOnClass(name = "com.qcloud.cos.COSClient")
    @ConditionalOnMissingBean(name = "tencentUploadStrategyImpl")
    public TencentUploadStrategyImpl tencentUploadStrategyImpl() {
        log.info("初始化腾讯云COS上传策略");
        return new TencentUploadStrategyImpl();
    }

    /**
     * 注册七牛云Kodo上传策略
     * 只有在七牛云SDK存在时才创建
     */
    @Bean("qiniuUploadStrategyImpl")
    @ConditionalOnClass(name = "com.qiniu.storage.UploadManager")
    @ConditionalOnMissingBean(name = "qiniuUploadStrategyImpl")
    public QiniuUploadStrategyImpl qiniuUploadStrategyImpl() {
        log.info("初始化七牛云Kodo上传策略");
        return new QiniuUploadStrategyImpl();
    }

    /**
     * 注册上传策略上下文
     */
    @Bean
    @ConditionalOnMissingBean
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