package ink.charter.website.common.file.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件上传策略
 *
 * @author charter
 * @create 2025/07/16
 */
public interface UploadStrategy {

    String uploadFile(MultipartFile file, String path);

    String uploadFile(String fileName, InputStream inputStream, String path);

}
