package ink.charter.website.common.file.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import ink.charter.website.common.auth.utils.SecurityUtils;
import ink.charter.website.common.core.common.enums.ResCodeEnum;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.common.core.enums.FileTypeEnum;
import ink.charter.website.common.core.enums.StatusEnum;
import ink.charter.website.common.core.exception.SystemException;
import ink.charter.website.common.core.utils.FileUtils;
import ink.charter.website.common.file.mapper.SysFilesMapper;
import ink.charter.website.common.file.service.CharterFileService;
import ink.charter.website.common.file.strategy.context.UploadStrategyContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件服务实现类
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CharterFileServiceImpl implements CharterFileService {

    private final SysFilesMapper sysFilesMapper;
    private final UploadStrategyContext uploadStrategyContext;

    @Override
    @Transactional
    public SysFilesEntity uploadFile(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) {
            throw SystemException.of("文件不能为空");
        }

        try {
            // 计算文件MD5
            String fileMd5 = calculateFileMd5(file);
            
            // 检查文件是否已存在
            SysFilesEntity existingFile = sysFilesMapper.selectByMd5(fileMd5);
            if (existingFile != null) {
                log.info("文件已存在，MD5: {}, 文件ID: {}", fileMd5, existingFile.getId());
                // 更新访问时间
                updateFileAccessTime(existingFile.getId());
                return existingFile;
            }

            // 智能添加路径层级“/”
            path = StrUtil.addSuffixIfNot(path, "/");

            // 上传文件到存储服务
            String fileUrl = uploadStrategyContext.executeUploadStrategy(file, path);
            
            // 保存文件信息到数据库
            SysFilesEntity fileEntity = new SysFilesEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setFileMd5(fileMd5);
            fileEntity.setFilePath(fileUrl);
            fileEntity.setFileType(getFileType(file.getOriginalFilename()));
            fileEntity.setUploadUserId(SecurityUtils.getCurrentUserId());
            fileEntity.setUploadTime(LocalDateTime.now());
            fileEntity.setLastAccessTime(LocalDateTime.now());
            fileEntity.setStatus(StatusEnum.ENABLED.getCode());
            
            sysFilesMapper.insert(fileEntity);
            
            log.info("文件上传成功，文件名: {}, 文件ID: {}, MD5: {}", 
                    file.getOriginalFilename(), fileEntity.getId(), fileMd5);
            
            return fileEntity;
            
        } catch (Exception e) {
            log.error("文件上传失败，文件名: {}", file.getOriginalFilename(), e);
            throw SystemException.of(ResCodeEnum.FILE_UPLOAD_FAILED, e);
        }
    }

    @Override
    @Transactional
    public SysFilesEntity uploadFile(String fileName, InputStream inputStream, String path) {
        if (StrUtil.isBlank(fileName) || inputStream == null) {
            throw SystemException.of("文件名和文件流不能为空");
        }

        try {
            // 计算文件MD5
            String fileMd5 = calculateFileMd5(inputStream);
            
            // 检查文件是否已存在
            SysFilesEntity existingFile = sysFilesMapper.selectByMd5(fileMd5);
            if (existingFile != null) {
                log.info("文件已存在，MD5: {}, 文件ID: {}", fileMd5, existingFile.getId());
                // 更新访问时间
                updateFileAccessTime(existingFile.getId());
                return existingFile;
            }

            // 智能添加路径层级“/”
            path = StrUtil.addSuffixIfNot(path, "/");

            // 重新创建输入流用于上传（因为计算MD5时已经读取过）
            // 注意：这里需要重新获取输入流，实际使用时可能需要调整
            String fileUrl = uploadStrategyContext.executeUploadStrategy(fileName, inputStream, path);
            
            // 保存文件信息到数据库
            SysFilesEntity fileEntity = new SysFilesEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setFileSize(0L); // 流方式无法直接获取大小
            fileEntity.setFileMd5(fileMd5);
            fileEntity.setFilePath(fileUrl);
            fileEntity.setFileType(getFileType(fileName));
            fileEntity.setUploadUserId(SecurityUtils.getCurrentUserId());
            fileEntity.setUploadTime(LocalDateTime.now());
            fileEntity.setLastAccessTime(LocalDateTime.now());
            fileEntity.setStatus(1);
            
            sysFilesMapper.insert(fileEntity);
            
            log.info("文件上传成功，文件名: {}, 文件ID: {}, MD5: {}", 
                    fileName, fileEntity.getId(), fileMd5);
            
            return fileEntity;
            
        } catch (Exception e) {
            log.error("文件上传失败，文件名: {}", fileName, e);
            throw SystemException.of(ResCodeEnum.FILE_UPLOAD_FAILED, e);
        }
    }

    @Override
    public SysFilesEntity getFileById(Long fileId) {
        if (fileId == null) {
            return null;
        }
        return sysFilesMapper.selectById(fileId);
    }

    @Override
    public SysFilesEntity getFileByMd5(String fileMd5) {
        if (StrUtil.isBlank(fileMd5)) {
            return null;
        }
        return sysFilesMapper.selectByMd5(fileMd5);
    }

    @Override
    public SysFilesEntity getFileByPath(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        return sysFilesMapper.selectByFilePath(filePath);
    }

    @Override
    public List<SysFilesEntity> getFilesByUserId() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            return List.of();
        }
        return sysFilesMapper.selectByUserId(currentUserId);
    }

    @Override
    public List<SysFilesEntity> getFilesByType(String fileType) {
        if (StrUtil.isBlank(fileType)) {
            return List.of();
        }
        return sysFilesMapper.selectByFileType(fileType);
    }

    @Override
    @Transactional
    public Boolean deleteFile(Long fileId) {
        if (fileId == null) {
            return false;
        }
        
        try {
            int result = sysFilesMapper.deleteById(fileId);
            boolean success = result > 0;
            
            if (success) {
                log.info("文件删除成功，文件ID: {}", fileId);
            } else {
                log.warn("文件删除失败，文件ID: {}", fileId);
            }
            
            return success;
        } catch (Exception e) {
            log.error("文件删除异常，文件ID: {}", fileId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public Integer deleteFiles(List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) {
            return 0;
        }
        
        try {
            int deletedCount = sysFilesMapper.deleteByIds(fileIds);
            log.info("批量删除文件完成，删除数量: {}, 总数量: {}", deletedCount, fileIds.size());
            return deletedCount;
        } catch (Exception e) {
            log.error("批量删除文件异常，文件ID列表: {}", fileIds, e);
            return 0;
        }
    }

    @Override
    public Boolean updateFileAccessTime(Long fileId) {
        if (fileId == null) {
            return false;
        }
        
        try {
            int result = sysFilesMapper.updateLastAccessTime(fileId, LocalDateTime.now());
            return result > 0;
        } catch (Exception e) {
            log.error("更新文件访问时间失败，文件ID: {}", fileId, e);
            return false;
        }
    }

    @Override
    public Boolean existsByMd5(String fileMd5) {
        if (StrUtil.isBlank(fileMd5)) {
            return false;
        }
        return sysFilesMapper.selectByMd5(fileMd5) != null;
    }

    @Override
    public String getFileDownloadUrl(Long fileId) {
        SysFilesEntity fileEntity = getFileById(fileId);
        if (fileEntity == null) {
            return null;
        }
        
        // 更新访问时间
        updateFileAccessTime(fileId);
        
        return fileEntity.getFilePath();
    }

    @Override
    public String getFileDownloadUrl(String filePath) {
        SysFilesEntity fileEntity = getFileByPath(filePath);
        if (fileEntity == null) {
            return null;
        }
        
        // 更新访问时间
        updateFileAccessTime(fileEntity.getId());
        
        return fileEntity.getFilePath();
    }

    @Override
    public String calculateFileMd5(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        try {
            return FileUtils.calculateMD5(file);
        } catch (Exception e) {
            log.error("计算文件MD5失败，文件名: {}", file.getOriginalFilename(), e);
            throw SystemException.of("计算文件MD5失败");
        }
    }

    @Override
    public String calculateFileMd5(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        
        try {
            byte[] bytes = IoUtil.readBytes(inputStream);
            return FileUtils.calculateMD5(bytes);
        } catch (Exception e) {
            log.error("计算文件MD5失败", e);
            throw SystemException.of("计算文件MD5失败");
        }
    }

    @Override
    public String getFileType(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return FileTypeEnum.OTHER.getMimeType();
        }
        
        // 处理路径分隔符，只取文件名部分
        String actualFileName = fileName;
        int lastSeparatorIndex = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if (lastSeparatorIndex != -1) {
            actualFileName = fileName.substring(lastSeparatorIndex + 1);
        }
        
        // 如果处理后的文件名为空，返回OTHER类型
        if (StrUtil.isBlank(actualFileName)) {
            return FileTypeEnum.OTHER.getMimeType();
        }
        
        int lastDotIndex = actualFileName.lastIndexOf('.');
        
        // 处理特殊情况：
        // 1. 没有扩展名
        // 2. 文件名以点结尾
        // 3. 隐藏文件（以点开头且只有一个点）
        if (lastDotIndex == -1 || lastDotIndex == actualFileName.length() - 1 || 
            (lastDotIndex == 0 && actualFileName.indexOf('.', 1) == -1)) {
            return FileTypeEnum.OTHER.getMimeType();
        }
        
        String extension = actualFileName.substring(lastDotIndex + 1).toLowerCase().trim();
        
        // 如果扩展名为空或包含非法字符，返回OTHER类型
        if (StrUtil.isBlank(extension) || extension.contains("/") || extension.contains("\\")) {
            return FileTypeEnum.OTHER.getMimeType();
        }
        
        FileTypeEnum fileTypeEnum = FileTypeEnum.getByExtension(extension);
        return fileTypeEnum.getMimeType();
    }
}