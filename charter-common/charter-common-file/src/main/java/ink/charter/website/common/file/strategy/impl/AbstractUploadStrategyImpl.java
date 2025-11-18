package ink.charter.website.common.file.strategy.impl;

import cn.hutool.core.io.IoUtil;
import ink.charter.website.common.core.exception.SystemException;
import ink.charter.website.common.core.common.enums.ResCodeEnum;
import ink.charter.website.common.file.strategy.UploadStrategy;
import ink.charter.website.common.core.utils.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件上传抽象类
 *
 * @author charter
 * @create 2025/07/16
 */
@Component
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String md5 = FileUtils.calculateMD5(file);
            String extName = FileUtils.getFileExtensionWithDot(file.getOriginalFilename());
            String mainName = FileUtils.getFileNameWithoutExtension(file.getOriginalFilename());
            // {md5}/{mainName}.{extName}，用md5作为文件存储上级目录，有效避免重复且上传文件名不改变
            String fileName = md5 + "/" + mainName + extName;
            if (!exists(path + fileName)) {
                upload(path, fileName, file.getInputStream());
            }
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            throw SystemException.of(ResCodeEnum.FILE_UPLOAD_FAILED, e);
        }
    }

    @Override
    public String uploadFile(String fileName, InputStream inputStream, String path) {
        try {
            byte[] bytes = IoUtil.readBytes(inputStream);
            String md5 = FileUtils.calculateMD5(bytes);
            // {md5}/{fileName}，用md5作为文件存储上级目录，有效避免重复且上传文件名不改变
            String uploadName = md5 + "/" + fileName;
            if (!exists(path + uploadName)) {
                upload(path, uploadName, inputStream);
            }
            return getFileAccessUrl(path + uploadName);
        } catch (Exception e) {
            throw SystemException.of(ResCodeEnum.FILE_UPLOAD_FAILED, e);
        }
    }

    public abstract Boolean exists(String filePath);

    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    public abstract String getFileAccessUrl(String filePath);

}
