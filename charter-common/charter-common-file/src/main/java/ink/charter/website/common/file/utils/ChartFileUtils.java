package ink.charter.website.common.file.utils;

import cn.hutool.core.util.StrUtil;
import ink.charter.website.common.auth.utils.SecurityUtils;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.common.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件操作工具类
 * 提供静态方法便于使用
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Component
public class ChartFileUtils {

    private static FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        ChartFileUtils.fileService = fileService;
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 存储路径
     * @return 文件信息
     */
    public static SysFilesEntity upload(MultipartFile file, String path) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return fileService.uploadFile(file, path, currentUserId);
    }

    /**
     * 上传文件（默认路径）
     *
     * @param file 文件
     * @return 文件信息
     */
    public static SysFilesEntity upload(MultipartFile file) {
        return upload(file, "default");
    }

    /**
     * 上传文件（流方式）
     *
     * @param fileName 文件名
     * @param inputStream 文件流
     * @param path 存储路径
     * @return 文件信息
     */
    public static SysFilesEntity upload(String fileName, InputStream inputStream, String path) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return fileService.uploadFile(fileName, inputStream, path, currentUserId);
    }

    /**
     * 上传文件（流方式，默认路径）
     *
     * @param fileName 文件名
     * @param inputStream 文件流
     * @return 文件信息
     */
    public static SysFilesEntity upload(String fileName, InputStream inputStream) {
        return upload(fileName, inputStream, "default");
    }

    /**
     * 根据文件ID获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    public static SysFilesEntity getById(Long fileId) {
        return fileService.getFileById(fileId);
    }

    /**
     * 根据MD5获取文件信息
     *
     * @param fileMd5 文件MD5值
     * @return 文件信息
     */
    public static SysFilesEntity getByMd5(String fileMd5) {
        return fileService.getFileByMd5(fileMd5);
    }

    /**
     * 根据文件路径获取文件信息
     *
     * @param filePath 文件路径
     * @return 文件信息
     */
    public static SysFilesEntity getByPath(String filePath) {
        return fileService.getFileByPath(filePath);
    }

    /**
     * 获取当前用户的文件列表
     *
     * @return 文件列表
     */
    public static List<SysFilesEntity> getCurrentUserFiles() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return fileService.getFilesByUserId(currentUserId);
    }

    /**
     * 根据文件类型获取文件列表
     *
     * @param fileType 文件类型
     * @return 文件列表
     */
    public static List<SysFilesEntity> getByType(String fileType) {
        return fileService.getFilesByType(fileType);
    }

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 是否删除成功
     */
    public static Boolean delete(Long fileId) {
        return fileService.deleteFile(fileId);
    }

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 删除成功的数量
     */
    public static Integer deleteFiles(List<Long> fileIds) {
        return fileService.deleteFiles(fileIds);
    }

    /**
     * 获取文件下载URL
     *
     * @param fileId 文件ID
     * @return 下载URL
     */
    public static String getDownloadUrl(Long fileId) {
        return fileService.getFileDownloadUrl(fileId);
    }

    /**
     * 获取文件下载URL（根据文件路径）
     *
     * @param filePath 文件路径
     * @return 下载URL
     */
    public static String getDownloadUrl(String filePath) {
        return fileService.getFileDownloadUrl(filePath);
    }

    /**
     * 检查文件是否存在（根据MD5）
     *
     * @param fileMd5 文件MD5值
     * @return 是否存在
     */
    public static Boolean exists(String fileMd5) {
        return fileService.existsByMd5(fileMd5);
    }

    /**
     * 计算文件MD5值
     *
     * @param file 文件
     * @return MD5值
     */
    public static String calculateMd5(MultipartFile file) {
        return fileService.calculateFileMd5(file);
    }

    /**
     * 计算文件MD5值（流方式）
     *
     * @param inputStream 文件流
     * @return MD5值
     */
    public static String calculateMd5(InputStream inputStream) {
        return fileService.calculateFileMd5(inputStream);
    }

    /**
     * 获取文件类型
     *
     * @param fileName 文件名
     * @return 文件类型
     */
    public static String getFileType(String fileName) {
        return fileService.getFileType(fileName);
    }

    /**
     * 更新文件访问时间
     *
     * @param fileId 文件ID
     * @return 是否更新成功
     */
    public static Boolean updateAccessTime(Long fileId) {
        return fileService.updateFileAccessTime(fileId);
    }

    /**
     * 检查文件名是否有效
     *
     * @param fileName 文件名
     * @return 是否有效
     */
    public static Boolean isValidFileName(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return false;
        }
        
        // 检查文件名是否包含非法字符
        String[] illegalChars = {"/", "\\", ":", "*", "?", "\"", "<", ">", "|"};
        for (String illegalChar : illegalChars) {
            if (fileName.contains(illegalChar)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 格式化文件大小
     *
     * @param fileSize 文件大小（字节）
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(Long fileSize) {
        if (fileSize == null || fileSize <= 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize.doubleValue();
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    /**
     * 检查文件类型是否为图片
     *
     * @param fileType 文件类型
     * @return 是否为图片
     */
    public static Boolean isImageType(String fileType) {
        if (StrUtil.isBlank(fileType)) {
            return false;
        }
        
        String[] imageTypes = {"jpg", "jpeg", "png", "gif", "bmp", "webp", "svg"};
        String lowerFileType = fileType.toLowerCase();
        
        for (String imageType : imageTypes) {
            if (imageType.equals(lowerFileType)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查文件类型是否为视频
     *
     * @param fileType 文件类型
     * @return 是否为视频
     */
    public static Boolean isVideoType(String fileType) {
        if (StrUtil.isBlank(fileType)) {
            return false;
        }
        
        String[] videoTypes = {"mp4", "avi", "mov", "wmv", "flv", "mkv", "webm"};
        String lowerFileType = fileType.toLowerCase();
        
        for (String videoType : videoTypes) {
            if (videoType.equals(lowerFileType)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查文件类型是否为文档
     *
     * @param fileType 文件类型
     * @return 是否为文档
     */
    public static Boolean isDocumentType(String fileType) {
        if (StrUtil.isBlank(fileType)) {
            return false;
        }
        
        String[] documentTypes = {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf"};
        String lowerFileType = fileType.toLowerCase();
        
        for (String documentType : documentTypes) {
            if (documentType.equals(lowerFileType)) {
                return true;
            }
        }
        
        return false;
    }

}