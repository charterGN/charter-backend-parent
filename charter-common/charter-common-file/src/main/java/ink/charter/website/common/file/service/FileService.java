package ink.charter.website.common.file.service;

import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件服务接口
 * 提供文件上传、下载、删除等常用操作
 *
 * @author charter
 * @create 2025/07/17
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 存储路径
     * @param uploadUserId 上传用户ID
     * @return 文件信息
     */
    SysFilesEntity uploadFile(MultipartFile file, String path, Long uploadUserId);

    /**
     * 上传文件（流方式）
     *
     * @param fileName 文件名
     * @param inputStream 文件流
     * @param path 存储路径
     * @param uploadUserId 上传用户ID
     * @return 文件信息
     */
    SysFilesEntity uploadFile(String fileName, InputStream inputStream, String path, Long uploadUserId);

    /**
     * 根据文件ID获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysFilesEntity getFileById(Long fileId);

    /**
     * 根据MD5获取文件信息
     *
     * @param fileMd5 文件MD5值
     * @return 文件信息
     */
    SysFilesEntity getFileByMd5(String fileMd5);

    /**
     * 根据文件路径获取文件信息
     *
     * @param filePath 文件路径
     * @return 文件信息
     */
    SysFilesEntity getFileByPath(String filePath);

    /**
     * 根据用户ID获取文件列表
     *
     * @param uploadUserId 上传用户ID
     * @return 文件列表
     */
    List<SysFilesEntity> getFilesByUserId(Long uploadUserId);

    /**
     * 根据文件类型获取文件列表
     *
     * @param fileType 文件类型
     * @return 文件列表
     */
    List<SysFilesEntity> getFilesByType(String fileType);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 是否删除成功
     */
    Boolean deleteFile(Long fileId);

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 删除成功的数量
     */
    Integer deleteFiles(List<Long> fileIds);

    /**
     * 更新文件访问时间
     *
     * @param fileId 文件ID
     * @return 是否更新成功
     */
    Boolean updateFileAccessTime(Long fileId);

    /**
     * 检查文件是否存在（根据MD5）
     *
     * @param fileMd5 文件MD5值
     * @return 是否存在
     */
    Boolean existsByMd5(String fileMd5);

    /**
     * 获取文件下载URL
     *
     * @param fileId 文件ID
     * @return 下载URL
     */
    String getFileDownloadUrl(Long fileId);

    /**
     * 获取文件下载URL（根据文件路径）
     *
     * @param filePath 文件路径
     * @return 下载URL
     */
    String getFileDownloadUrl(String filePath);

    /**
     * 计算文件MD5值
     *
     * @param file 文件
     * @return MD5值
     */
    String calculateFileMd5(MultipartFile file);

    /**
     * 计算文件MD5值（流方式）
     *
     * @param inputStream 文件流
     * @return MD5值
     */
    String calculateFileMd5(InputStream inputStream);

    /**
     * 获取文件类型
     *
     * @param fileName 文件名
     * @return 文件类型
     */
    String getFileType(String fileName);

}