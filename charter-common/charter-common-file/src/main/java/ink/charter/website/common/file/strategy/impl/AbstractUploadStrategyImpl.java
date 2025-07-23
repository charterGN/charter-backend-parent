package ink.charter.website.common.file.strategy.impl;

import ink.charter.website.common.file.constant.SeparatorConstant;
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
        // TODO 上传逻辑要改，后续数据库建一个文件表，存储文件信息
        try {
            String md5 = FileUtils.calculateMD5(file);
            String extName = FileUtils.getFileExtensionWithDot(file.getOriginalFilename());
            String mainName = FileUtils.getFileNameWithoutExtension(file.getOriginalFilename());
            String fileName = mainName + SeparatorConstant.FILENAME_SEPARATOR + md5 + extName;
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
        // TODO 上传逻辑要改，后续数据库建一个文件表，存储文件信息
        try {
            upload(path, fileName, inputStream);
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            throw SystemException.of(ResCodeEnum.FILE_UPLOAD_FAILED, e);
        }
    }

    public abstract Boolean exists(String filePath);

    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    public abstract String getFileAccessUrl(String filePath);

}
