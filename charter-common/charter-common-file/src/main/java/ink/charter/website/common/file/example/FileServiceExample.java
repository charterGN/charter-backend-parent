package ink.charter.website.common.file.example;

import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.common.file.service.FileService;
import ink.charter.website.common.file.utils.ChartFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务使用示例
 * 展示如何使用文件服务进行各种文件操作
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileServiceExample {

    private final FileService fileService;

    /**
     * 示例1：上传文件
     */
    public void uploadFileExample(MultipartFile file) {
        try {
            // 方式1：使用工具类上传到指定路径
            SysFilesEntity fileEntity = ChartFileUtils.upload(file, "uploads/images");
            log.info("文件上传成功，文件ID: {}, 文件名: {}", fileEntity.getId(), fileEntity.getFileName());
            
            // 方式2：使用默认路径
            SysFilesEntity fileEntity2 = ChartFileUtils.upload(file);
            log.info("文件上传成功，文件ID: {}, 文件名: {}", fileEntity2.getId(), fileEntity2.getFileName());
            
        } catch (Exception e) {
            log.error("文件上传失败", e);
        }
    }

    /**
     * 示例2：检查文件是否已存在（秒传功能）
     */
    public void checkFileExistsExample(MultipartFile file) {
        try {
            // 计算文件MD5
            String fileMd5 = ChartFileUtils.calculateMd5(file);
            
            // 检查文件是否已存在
            if (ChartFileUtils.exists(fileMd5)) {
                log.info("文件已存在，可以实现秒传功能");
                SysFilesEntity existingFile = ChartFileUtils.getByMd5(fileMd5);
                log.info("已存在的文件信息: {}", existingFile.getFileName());
            } else {
                log.info("文件不存在，需要上传");
            }
            
        } catch (Exception e) {
            log.error("检查文件存在性失败", e);
        }
    }

    /**
     * 示例3：获取文件信息
     */
    public void getFileInfoExample(Long fileId) {
        try {
            // 根据ID获取文件信息
            SysFilesEntity fileEntity = ChartFileUtils.getById(fileId);
            if (fileEntity != null) {
                log.info("文件名: {}", fileEntity.getFileName());
                log.info("文件大小: {}", ChartFileUtils.formatFileSize(fileEntity.getFileSize()));
                log.info("文件类型: {}", fileEntity.getFileType());
                log.info("上传时间: {}", fileEntity.getUploadTime());
                
                // 判断文件类型
                if (ChartFileUtils.isImageType(fileEntity.getFileType())) {
                    log.info("这是一个图片文件");
                } else if (ChartFileUtils.isVideoType(fileEntity.getFileType())) {
                    log.info("这是一个视频文件");
                } else if (ChartFileUtils.isDocumentType(fileEntity.getFileType())) {
                    log.info("这是一个文档文件");
                }
            }
            
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
        }
    }

    /**
     * 示例4：获取文件下载链接
     */
    public void getDownloadUrlExample(Long fileId) {
        try {
            String downloadUrl = ChartFileUtils.getDownloadUrl(fileId);
            if (downloadUrl != null) {
                log.info("文件下载链接: {}", downloadUrl);
                // 这里会自动更新文件的最后访问时间
            } else {
                log.warn("文件不存在或已被删除");
            }
            
        } catch (Exception e) {
            log.error("获取下载链接失败", e);
        }
    }

    /**
     * 示例5：查询当前用户的所有文件
     */
    public void getCurrentUserFilesExample() {
        try {
            List<SysFilesEntity> userFiles = ChartFileUtils.getCurrentUserFiles();
            log.info("当前用户共有 {} 个文件", userFiles.size());
            
            for (SysFilesEntity file : userFiles) {
                log.info("文件: {} ({})", file.getFileName(), 
                        ChartFileUtils.formatFileSize(file.getFileSize()));
            }
            
        } catch (Exception e) {
            log.error("查询当前用户文件失败", e);
        }
    }

    /**
     * 示例6：根据文件类型查询文件
     */
    public void getFilesByTypeExample(String fileType) {
        try {
            List<SysFilesEntity> files = ChartFileUtils.getByType(fileType);
            log.info("类型为 {} 的文件共有 {} 个", fileType, files.size());
            
        } catch (Exception e) {
            log.error("根据类型查询文件失败", e);
        }
    }

    /**
     * 示例7：删除文件
     */
    public void deleteFileExample(Long fileId) {
        try {
            Boolean success = ChartFileUtils.delete(fileId);
            if (success) {
                log.info("文件删除成功");
            } else {
                log.warn("文件删除失败");
            }
            
        } catch (Exception e) {
            log.error("删除文件失败", e);
        }
    }

    /**
     * 示例8：批量删除文件
     */
    public void batchDeleteFilesExample(List<Long> fileIds) {
        try {
            Integer deletedCount = ChartFileUtils.deleteFiles(fileIds);
            log.info("批量删除完成，成功删除 {} 个文件", deletedCount);
            
        } catch (Exception e) {
            log.error("批量删除文件失败", e);
        }
    }

    /**
     * 示例9：文件名验证
     */
    public void validateFileNameExample(String fileName) {
        if (ChartFileUtils.isValidFileName(fileName)) {
            log.info("文件名 '{}' 是有效的", fileName);
        } else {
            log.warn("文件名 '{}' 包含非法字符", fileName);
        }
    }

    /**
     * 示例10：在Controller中的使用方式
     */
    public void controllerUsageExample() {
        /*
        @RestController
        @RequestMapping("/api/files")
        @RequiredArgsConstructor
        public class FileController {
        
            private final FileService fileService;
        
            @PostMapping("/upload")
            public Result<FileVO> uploadFile(@RequestParam("file") MultipartFile file,
                                           @RequestParam("path") String path) {
                // 获取当前用户ID（从SecurityContext或其他方式）
                Long currentUserId = getCurrentUserId();
                
                // 上传文件
                SysFilesEntity fileEntity = ChartFileUtils.upload(file, path, currentUserId);
                
                // 转换为VO返回
                FileVO fileVO = new FileVO();
                fileVO.setId(fileEntity.getId());
                fileVO.setFileName(fileEntity.getFileName());
                fileVO.setFileSize(ChartFileUtils.formatFileSize(fileEntity.getFileSize()));
                fileVO.setFileType(fileEntity.getFileType());
                fileVO.setDownloadUrl(ChartFileUtils.getDownloadUrl(fileEntity.getId()));
                
                return Result.success(fileVO);
            }
            
            @GetMapping("/download/{fileId}")
            public void downloadFile(@PathVariable Long fileId, HttpServletResponse response) {
                String downloadUrl = ChartFileUtils.getDownloadUrl(fileId);
                if (downloadUrl != null) {
                    // 重定向到实际下载地址
                    response.sendRedirect(downloadUrl);
                } else {
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                }
            }
            
            @PostMapping("/{fileId}")
            public Result<Void> deleteFile(@PathVariable Long fileId) {
                Boolean success = ChartFileUtils.delete(fileId);
                return success ? Result.success() : Result.error("删除失败");
            }
        }
        */
    }

}