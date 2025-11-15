# Charter 文件模块

## 模块概述

`charter-common-file` 是 Charter 项目的通用文件处理模块，提供统一的文件上传、下载、管理功能，支持多种云存储服务。

## 核心特性

- **多云存储支持**：支持阿里云OSS、MinIO、华为云OBS、腾讯云COS、七牛云Kodo
- **灵活配置**：支持配置文件和数据库两种配置方式，数据库配置优先级更高
- **文件去重**：基于MD5自动识别重复文件，避免重复存储
- **统一接口**：提供统一的文件操作接口，屏蔽底层存储差异
- **自动装配**：基于Spring Boot自动配置，开箱即用

## 技术架构

### 核心组件

```
charter-common-file
├── config                    # 配置类
│   ├── FileConfigProperties  # 文件配置属性
│   └── FileAutoConfiguration # 自动配置类
├── mapper                    # 数据访问层
│   ├── SysFilesMapper        # 文件信息Mapper
│   └── SysFileConfigMapper   # 文件配置Mapper
├── service                   # 服务层
│   ├── CharterFileService           # 文件服务接口
│   └── impl
│       └── CharterFileServiceImpl   # 文件服务实现
├── strategy                  # 策略模式
│   ├── UploadStrategy        # 上传策略接口
│   ├── context
│   │   └── UploadStrategyContext  # 策略上下文
│   └── impl                  # 各云服务实现
│       ├── AbstractUploadStrategyImpl
│       ├── OssUploadStrategyImpl      # 阿里云OSS
│       ├── MinioUploadStrategyImpl    # MinIO
│       ├── HuaweiUploadStrategyImpl   # 华为云OBS
│       ├── TencentUploadStrategyImpl  # 腾讯云COS
│       └── QiniuUploadStrategyImpl    # 七牛云Kodo
├── enums                     # 枚举类
│   └── UploadModeEnum        # 上传模式枚举
└── utils                     # 工具类
    └── ChartFileUtils        # 文件工具类
```

### 设计模式

- **策略模式**：不同云服务的上传策略实现
- **模板方法模式**：AbstractUploadStrategyImpl 定义上传流程模板
- **工厂模式**：UploadStrategyContext 根据配置选择具体策略

## 配置说明

### 配置优先级

**数据库配置 > 配置文件配置**

系统启动时会优先查询数据库中启用的配置，如果没有则使用配置文件中的配置。

### 配置文件方式

在 `application.yml` 中配置：

```yaml
charter:
  file:
    storage-type: oss                    # 存储类型
    domain: https://your-domain.com      # 访问域名
    endpoint: oss-cn-hangzhou.aliyuncs.com  # 端点地址
    region: ap-guangzhou                 # 区域（腾讯云需要）
    access-key-id: your-access-key-id    # 访问密钥ID
    access-key-secret: your-secret       # 访问密钥Secret
    bucket-name: your-bucket             # 存储桶名称
```

### 各云服务配置示例

#### 1. 阿里云OSS

```yaml
charter:
  file:
    storage-type: oss
    domain: https://your-bucket.oss-cn-hangzhou.aliyuncs.com
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: LTAI5t...
    access-key-secret: xxx...
    bucket-name: your-bucket
```

#### 2. MinIO

```yaml
charter:
  file:
    storage-type: minio
    domain: http://localhost:9000
    endpoint: http://localhost:9000
    access-key-id: minioadmin
    access-key-secret: minioadmin
    bucket-name: your-bucket
```

#### 3. 华为云OBS

```yaml
charter:
  file:
    storage-type: huawei
    domain: https://your-bucket.obs.cn-north-4.myhuaweicloud.com
    endpoint: obs.cn-north-4.myhuaweicloud.com
    access-key-id: xxx...
    access-key-secret: xxx...
    bucket-name: your-bucket
```

#### 4. 腾讯云COS

```yaml
charter:
  file:
    storage-type: tencent
    domain: https://your-bucket-1234567890.cos.ap-guangzhou.myqcloud.com
    endpoint: cos.ap-guangzhou.myqcloud.com
    region: ap-guangzhou
    access-key-id: AKIDxxx...
    access-key-secret: xxx...
    bucket-name: your-bucket-1234567890
```

#### 5. 七牛云Kodo

```yaml
charter:
  file:
    storage-type: qiniu
    domain: https://your-domain.com
    access-key-id: xxx...
    access-key-secret: xxx...
    bucket-name: your-bucket
```

## 使用指南

### 依赖引入

在项目 `pom.xml` 中引入：

```xml
<dependency>
    <groupId>ink.charter</groupId>
    <artifactId>charter-common-file</artifactId>
    <version>${revision}</version>
</dependency>
```

**云服务SDK依赖**：

模块已内置所有主流云服务SDK，无需额外引入依赖。支持的云服务包括：
- ✅ 阿里云OSS
- ✅ MinIO
- ✅ 华为云OBS
- ✅ 腾讯云COS
- ✅ 七牛云Kodo

**所有SDK已自动引入，可以在页面配置或数据库中直接切换使用，无需重启应用。**

### 代码示例

#### 1. 注入服务

```java
@Autowired
private CharterFileService charterFileService;
```

#### 2. 文件上传

```java
// 上传文件
SysFilesEntity file = charterFileService.uploadFile(multipartFile, "/upload/images/", userId);

// 获取文件信息
Long fileId = file.getId();
String filePath = file.getFilePath();
String fileUrl = file.getFilePath(); // 完整访问URL
```

#### 3. 文件查询

```java
// 根据ID查询
SysFilesEntity file = charterFileService.getFileById(fileId);

// 根据MD5查询
SysFilesEntity file = charterFileService.getFileByMd5(md5);

// 查询用户的所有文件
List<SysFilesEntity> files = charterFileService.getFilesByUserId(userId);

// 根据文件类型查询
List<SysFilesEntity> images = charterFileService.getFilesByType("image/jpeg");
```

#### 4. 文件下载

```java
// 获取下载URL
String downloadUrl = charterFileService.getFileDownloadUrl(fileId);

// 或根据文件路径获取
String d
ownloadUrl = charterFileService.getFileDownloadUrl(filePath);
```

#### 5. 文件删除

```java
// 删除单个文件
Boolean success = charterFileService.deleteFile(fileId);

// 批量删除
List<Long> fileIds = Arrays.asList(1L, 2L, 3L);
Integer deletedCount = charterFileService.deleteFiles(fileIds);
```

#### 6. 文件工具类

```java
// 使用工具类简化操作
SysFilesEntity file = ChartFileUtils.upload(multipartFile);
String downloadUrl = ChartFileUtils.getDownloadUrl(fileId);
Boolean success = ChartFileUtils.delete(fileId);

// 文件类型判断
Boolean isImage = ChartFileUtils.isImageType(fileType);
Boolean isVideo = ChartFileUtils.isVideoType(fileType);
Boolean isDocument = ChartFileUtils.isDocumentType(fileType);

// 文件大小格式化
String sizeStr = ChartFileUtils.formatFileSize(fileSize); // 如：1.5 MB
```

## 核心功能

### 1. 文件去重

系统基于MD5自动识别重复文件：
- 上传文件时自动计算MD5
- 如果MD5已存在，直接返回已有文件信息
- 避免重复存储，节省存储空间

### 2. 访问时间追踪

系统自动记录文件访问时间：
- 上传时记录 `upload_time` 和 `last_access_time`
- 下载时自动更新 `last_access_time`
- 便于统计文件使用情况

### 3. 文件类型识别

系统根据文件扩展名自动识别文件类型：
- 支持图片、视频、文档、音频等常见类型
- 返回标准的MIME类型
- 可用于前端展示和权限控制

### 4. 动态策略切换

系统支持运行时动态切换存储策略：
- 修改数据库配置即可切换云服务
- 无需重启应用
- 配置变更立即生效

## 扩展开发

### 添加新的云服务支持

1. **创建策略实现类**

```java
@Slf4j
@Component("xxxUploadStrategyImpl")
public class XxxUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private FileConfigProperties fileConfigProperties;

    @Override
    public Boolean exists(String filePath) {
        // 实现文件存在性检查
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        // 实现文件上传逻辑
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        // 返回文件访问URL
        return fileConfigProperties.getDomain() + "/" + filePath;
    }
}
```

2. **更新上传模式枚举**

```java
public enum UploadModeEnum {
    // ... 其他枚举
    XXX("xxx", "xxxUploadStrategyImpl");
}
```

3. **注册Bean**

在 `FileAutoConfiguration` 中添加：

```java
@Bean("xxxUploadStrategyImpl")
@ConditionalOnMissingBean(name = "xxxUploadStrategyImpl")
public XxxUploadStrategyImpl xxxUploadStrategyImpl() {
    log.info("初始化XXX上传策略");
    return new XxxUploadStrategyImpl();
}
```

4. **添加依赖**

在 `charter-dependencies/pom.xml` 中添加版本管理，在 `charter-common-file/pom.xml` 中引入依赖。

## 常见问题

### Q1: 如何切换云服务？

**A**: 有两种方式，**都无需重启应用**：
1. 修改配置文件中的 `charter.file.storage-type`（需要重新加载配置）
2. 在数据库 `sys_file_config` 表中更新启用的配置（推荐，立即生效）

```sql
-- 切换到MinIO
UPDATE sys_file_config SET enabled = 0 WHERE storage_type = 'oss';
UPDATE sys_file_config SET enabled = 1 WHERE storage_type = 'minio';
```

### Q2: 数据库配置和配置文件配置有什么区别？

**A**: 
- 数据库配置优先级更高，会覆盖配置文件配置
- 数据库配置可以动态修改，**立即生效，无需重启应用**
- 配置文件配置作为默认配置，当数据库没有启用配置时使用
- 所有云服务SDK已内置，可以随时切换

### Q3: 如何实现文件的权限控制？

**A**: 
- 在 `CharterFileService` 基础上封装业务层服务
- 在业务层实现权限校验逻辑
- 可以扩展 `sys_files` 表添加权限字段

### Q4: 支持分片上传吗？

**A**: 
- 当前版本不支持分片上传
- 如需支持，可以扩展 `UploadStrategy` 接口
- 各云服务SDK都提供了分片上传API

### Q5: 如何处理文件删除？

**A**: 
- 当前实现是逻辑删除（软删除）
- 物理删除需要定时任务清理云存储中的文件
- 建议保留一段时间后再物理删除

## 技术支持

如有问题或建议，请联系：
- 作者：charter
- 邮箱：2088694379@qq.com
- 网站：https://www.charter.ink
